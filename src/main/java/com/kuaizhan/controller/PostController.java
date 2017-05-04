package com.kuaizhan.controller;

import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.exception.business.*;
import com.kuaizhan.exception.common.DownloadFileFailedException;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.JsonParseException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.param.PostsParam;
import com.kuaizhan.param.UploadPicParam;
import com.kuaizhan.param.WxSyncsPostParam;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.pojo.VO.JsonResponse;
import com.kuaizhan.pojo.VO.PostListFlatVO;
import com.kuaizhan.pojo.VO.PostListVO;
import com.kuaizhan.pojo.VO.PostVO;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.PostService;
import com.kuaizhan.service.WeixinPostService;
import com.kuaizhan.utils.JsonUtil;
import com.kuaizhan.utils.PojoSwitcher;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图文消息controller
 * Created by zixiong on 2017/3/20.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION, produces = "application/json")
public class PostController extends BaseController {

    @Resource
    PostService postService;
    @Resource
    AccountService accountService;
    @Resource
    PojoSwitcher pojoSwitcher;
    @Resource
    WeixinPostService weixinPostService;

    private static final Logger logger = Logger.getLogger(PostController.class);


    /**
     * 获取多图文列表
     *
     * @throws DaoException
     */

    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public JsonResponse listPostByPagination(@RequestParam long weixinAppid, @RequestParam int page, @RequestParam(required = false) String title, @RequestParam(defaultValue = "0") Boolean flat)
            throws DaoException {

        Page<PostDO> postDOPage = postService.listPostsByPagination(weixinAppid, title, page, flat);
        List<PostDO> postDOList = postDOPage.getResult();

        // 展开多图文
        if (flat) {
            PostListFlatVO postListFlatVO = new PostListFlatVO();

            postListFlatVO.setTotalNum(postDOPage.getTotalCount());
            postListFlatVO.setCurrentPage(postDOPage.getPageNo());
            postListFlatVO.setTotalPage(postDOPage.getTotalPages());

            for (PostDO postDO: postDOList) {
                postListFlatVO.getPosts().add(pojoSwitcher.postDOToVO(postDO));
            }

            return new JsonResponse(postListFlatVO);

        // 不展开多图文
        } else {
            PostListVO postListVO = new PostListVO();

            postListVO.setTotalNum(postDOPage.getTotalCount());
            postListVO.setCurrentPage(postDOPage.getPageNo());
            postListVO.setTotalPage(postDOPage.getTotalPages());

            if (postDOList != null) {

                for (PostDO postDO : postDOList) {

                    // 多图文实体是一个list
                    List<PostVO> multiPostVOList = new ArrayList<>();

                    // 获取图文总记录下面的多图文
                    if (postDO.getType() == 2) {
                        List<PostDO> multiPostDOList = postService.listMultiPosts(weixinAppid, postDO.getMediaId(), false);
                        if (multiPostDOList != null) {
                            for (PostDO multiPostDo : multiPostDOList) {
                                PostVO multiPostVO = pojoSwitcher.postDOToVO(multiPostDo);
                                multiPostVOList.add(multiPostVO);
                            }
                            postListVO.getPosts().add(multiPostVOList);
                        } else {
                            // 发现垃圾数据
                            logger.error("【图文垃圾数据】总记录下没有多图文, pageId:" + postDO.getPageId());
                        }
                    // 单图文
                    } else {
                        PostVO postVO = pojoSwitcher.postDOToVO(postDO);

                        multiPostVOList.add(postVO);
                        postListVO.getPosts().add(multiPostVOList);
                    }
                }
            }
            return new JsonResponse(postListVO);
        }
    }


    /**
     * 获取单图文详情
     *
     * @return
     */
    @RequestMapping(value = "/posts/{pageId}", method = RequestMethod.GET)
    public JsonResponse getPost(@PathVariable long pageId) throws DaoException {
        PostDO postDO = postService.getPostByPageId(pageId);
        PostVO postVO = pojoSwitcher.postDOToVO(postDO);
        return new JsonResponse(postVO);
    }

    /**
     * 获取多图文详情
     *
     * @return
     */
    @RequestMapping(value = "/multi_posts/{pageId}", method = RequestMethod.GET)
    public JsonResponse getMultiPost(@PathVariable long pageId) throws DaoException {
        PostDO postDO = postService.getPostByPageId(pageId);

        List<PostVO> multiPostVOList = new ArrayList<>();
        if (postDO != null) {
            // 如果图文type为3（多图文中的一条），根据mediaId找出多图文
            if (postDO.getType() == 3) {
                List<PostDO> multiPostDOList = postService.listMultiPosts(postDO.getWeixinAppid(), postDO.getMediaId(), true);
                for (PostDO multiPostDO : multiPostDOList) {
                    multiPostVOList.add(pojoSwitcher.postDOToVO(multiPostDO));
                }
            } else {
                multiPostVOList.add(pojoSwitcher.postDOToVO(postDO));
            }
        }
        return new JsonResponse(multiPostVOList);
    }

    /**
     * 新增多图文
     */
    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    public JsonResponse insertPost(@Valid @RequestBody PostsParam postsParam) throws Exception {

        postService.insertMultiPosts(postsParam.getWeixinAppid(), postsParam.getPostDOs());
        return new JsonResponse(null);
    }

    /**
     * 修改多图文
     */
    @RequestMapping(value = "/posts/{pageId}", method = RequestMethod.PUT)
    public JsonResponse updatePost(@PathVariable("pageId") long pageId, @Valid @RequestBody PostsParam postsParam) throws Exception {
        postService.updateMultiPosts(postsParam.getWeixinAppid(), pageId, postsParam.getPostDOs());
        return new JsonResponse(null);
    }

    /**
     * 删除图文
     *
     * @param weixinAppid
     * @param pageId
     */
    @RequestMapping(value = "/posts/{pageId}", method = RequestMethod.DELETE)
    public JsonResponse deletePost(@RequestParam long weixinAppid, @PathVariable long pageId) throws RedisException, AccountNotExistException, JsonParseException, DaoException {
        postService.deletePost(weixinAppid, pageId, accountService.getAccessToken(weixinAppid));
        return new JsonResponse(null);
    }

    /**
     * 一键同步微信文章到快站微信
     *
     * @return
     */
    @RequestMapping(value = "/posts/wx_syncs", method = RequestMethod.POST)
    public JsonResponse wxSyncsPost(@Valid @RequestBody WxSyncsPostParam wxSyncsPostParam) throws ParamException {
        postService.syncWeixinPosts(wxSyncsPostParam.getWeixinAppid(), wxSyncsPostParam.getUid());
        return new JsonResponse(null);
    }


    /**
     * 快站微信同步到快站文章
     *
     * @return
     */
    @RequestMapping(value = "/posts/kzweixin_syncs", method = RequestMethod.POST)
    public JsonResponse kzweixinSyncs2KzPost(@RequestBody String postData) throws DaoException, IOException, AccountNotExistException, RedisException, JsonParseException {
        JSONObject jsonObject = new JSONObject(postData);
        Long weixinAppid = jsonObject.optLong("weixinAppid");
        AccountDO accountDO = accountService.getAccountByWeixinAppId(weixinAppid);
        Long categoryId = jsonObject.optLong("categoryId");
        List<Long> pageIds = JsonUtil.string2List(jsonObject.get("pageIds").toString(), Long.class);
        for (Long pageId: pageIds){
            postService.export2KzArticle(pageId, categoryId, accountDO.getSiteId());
        }
        return new JsonResponse(null);
    }


    /**
     * 从快站文章导入到快站微信
     *
     * @throws IOException
     */
    @RequestMapping(value = "/posts/kz_imports", method = RequestMethod.POST)
    public JsonResponse kzSyncsPost(@RequestBody String postData) throws IOException {
        JSONObject jsonObject = new JSONObject(postData);

        Long weixinAppid = jsonObject.optLong("weixinAppid");

        List<Long> pageIds = JsonUtil.string2List(jsonObject.get("pageIds").toString(), Long.class);
        postService.importKzArticle(weixinAppid, pageIds);
        return new JsonResponse(null);
    }

    /**
     * 上传素材到微信服务器
     * @return
     */
    @RequestMapping(value = "/weixin_materials", method = RequestMethod.POST)
    public JsonResponse uploadWeixinThumb(@Valid @RequestBody UploadPicParam uploadPicParam) throws RedisException, JsonParseException, DaoException, AccountNotExistException, DownloadFileFailedException {
        Map result = weixinPostService.uploadImage(accountService.getAccessToken(uploadPicParam.getWeixinAppid()), uploadPicParam.getImgUrl());

        return new JsonResponse(result);
    }

    @RequestMapping(value = "weixin_pics", method = RequestMethod.POST)
    public JsonResponse uploadWeixinPic(@Valid @RequestBody UploadPicParam uploadPicParam) throws DaoException, AccountNotExistException, RedisException, DownloadFileFailedException {
        String url = weixinPostService.uploadImgForPost(accountService.getAccessToken(uploadPicParam.getWeixinAppid()), uploadPicParam.getImgUrl());
        Map<String ,String> result = new HashMap<>();
        result.put("url", url);
        return new JsonResponse(result);
    }

    @RequestMapping(value = "/posts/{pageId}/wx_url", method = RequestMethod.GET)
    public JsonResponse getPostWxUrl(@PathVariable("pageId") long pageId, @RequestParam long weixinAppid) throws DaoException, RedisException, AccountNotExistException {
        Map<String ,String> result = new HashMap<>();
        result.put("wxUrl", postService.getPostWxUrl(weixinAppid, pageId));
        return new JsonResponse(result);
    }
}
