package com.kuaizhan.controller;

import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.exception.business.*;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.JsonParseException;
import com.kuaizhan.exception.system.MongoException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.param.PostsParam;
import com.kuaizhan.param.WxSyncsPostParam;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.pojo.DTO.WxPostDTO;
import com.kuaizhan.pojo.VO.JsonResponse;
import com.kuaizhan.pojo.VO.PostListVO;
import com.kuaizhan.pojo.VO.PostVO;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.PostService;
import com.kuaizhan.service.WeixinPostService;
import com.kuaizhan.utils.JsonUtil;
import com.kuaizhan.utils.PojoSwitcher;
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


    /**
     * 获取多图文列表
     *
     * @throws DaoException
     */

    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public JsonResponse listPostByPagination(@RequestParam long weixinAppid, @RequestParam int page, @RequestParam(required = false) String title)
            throws DaoException, MongoException {

        Page<PostDO> postDOPage = postService.listPostsByPagination(weixinAppid, title, page);

        List<PostDO> postDOList = postDOPage.getResult();

        PostListVO postListVO = new PostListVO();

        if (postDOList != null) {
            postListVO.setTotalNum(postDOPage.getTotalCount());
            postListVO.setCurrentPage(postDOPage.getPageNo());
            postListVO.setTotalPage(postDOPage.getTotalPages());

            for (PostDO postDO : postDOList) {

                // 多图文实体是一个list
                List<PostVO> multiPostVOList = new ArrayList<>();

                // 获取图文总记录下面的多图文
                if (postDO.getType() == 2) {
                    List<PostDO> multiPostDOList = postService.listMultiPosts(weixinAppid, postDO.getMediaId());
                    if (multiPostDOList != null) {
                        for (PostDO multiPostDo : multiPostDOList) {
                            PostVO multiPostVO = pojoSwitcher.postDOToVO(multiPostDo);
                            multiPostVOList.add(multiPostVO);
                        }
                        postListVO.getPosts().add(multiPostVOList);
                    }
                } else {
                    PostVO postVO = pojoSwitcher.postDOToVO(postDO);

                    multiPostVOList.add(postVO);
                    postListVO.getPosts().add(multiPostVOList);
                }
            }
        }
        return new JsonResponse(postListVO);
    }


    /**
     * 获取单图文详情
     *
     * @return
     */
    @RequestMapping(value = "/posts/{pageId}", method = RequestMethod.GET)
    public JsonResponse getPost(@PathVariable long pageId) throws DaoException, MongoException {
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
    public JsonResponse getMultiPost(@PathVariable long pageId) throws DaoException, MongoException {
        PostDO postDO = postService.getPostByPageId(pageId);

        List<PostVO> multiPostVOList = new ArrayList<>();
        if (postDO != null) {
            // 如果图文type为3（多图文中的一条），根据mediaId找出多图文
            if (postDO.getType() == 3) {
                List<PostDO> multiPostDOList = postService.listMultiPosts(postDO.getWeixinAppid(), postDO.getMediaId());

                for (PostDO multipostDO : multiPostDOList) {

                    multiPostVOList.add(pojoSwitcher.postDOToVO(multipostDO));
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
    public JsonResponse deletePost(@RequestParam long weixinAppid, @PathVariable long pageId) throws RedisException, DaoException, AccountNotExistException, MaterialDeleteException, JsonParseException, MongoException {
        postService.deletePost(weixinAppid, pageId, accountService.getAccessToken(weixinAppid));
        return new JsonResponse(null);
    }

    /**
     * 一键同步微信文章到快站微信
     *
     * @return
     */
    @RequestMapping(value = "/posts/wx_syncs", method = RequestMethod.POST)
    public JsonResponse wxSyncsPost(@Valid @RequestBody WxSyncsPostParam wxSyncsPostParam) throws ParamException, SyncWXPostTooOftenException {
        postService.syncWeixinPosts(wxSyncsPostParam.getWeixinAppid(), wxSyncsPostParam.getUid());
        return new JsonResponse(null);
    }


    /**
     * 快站微信同步到快站文章
     *
     * @return
     */
    @RequestMapping(value = "/posts/kzweixin_syncs", method = RequestMethod.POST)
    public JsonResponse kzweixinSyncs2KzPost(@RequestBody String postData) throws KZPostAddException, DaoException, MongoException, IOException, AccountNotExistException, RedisException, JsonParseException {
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
     * @param postData
     * @return
     */
    @RequestMapping(value = "/weixin_materials", method = RequestMethod.POST)
    public JsonResponse uploadWeixinThumb(@RequestBody String postData) throws ParamException, RedisException,
            JsonParseException, DaoException, AccountNotExistException, AddMaterialException, DownloadFileFailedException {
        JSONObject jsonObject = new JSONObject(postData);
        Long weixinAppid = jsonObject.optLong("weixinAppid");
        String imgUrl = jsonObject.optString("imgUrl");
        if (weixinAppid == 0 || imgUrl == null){
            throw new ParamException();
        }
        Map result = weixinPostService.uploadImage(accountService.getAccessToken(weixinAppid), imgUrl);

        return new JsonResponse(result);
    }

    @RequestMapping(value = "/posts/{pageId}/wx_url", method = RequestMethod.GET)
    public JsonResponse getPostWxUrl(@PathVariable("pageId") long pageId, @RequestParam long weixinAppid) throws MongoException, DaoException, RedisException, AccountNotExistException, WxPostDeletedException {
        PostDO postDO = postService.getPostByPageId(pageId);
        List<WxPostDTO> wxPostDTOS = weixinPostService.getWxPost(postDO.getMediaId(), accountService.getAccessToken(weixinAppid));
        String wxUrl = wxPostDTOS.get(postDO.getIndex()).getUrl();

        Map<String ,String> result = new HashMap<>();
        result.put("wxUrl", wxUrl);
        return new JsonResponse(result);
    }
}
