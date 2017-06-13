package com.kuaizhan.controller;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.exception.deprecated.business.*;
import com.kuaizhan.exception.common.DownloadFileFailedException;
import com.kuaizhan.controller.param.PostsParam;
import com.kuaizhan.controller.param.UploadPicParam;
import com.kuaizhan.controller.param.WxSyncsPostParam;
import com.kuaizhan.pojo.po.AccountPO;
import com.kuaizhan.pojo.po.PostPO;
import com.kuaizhan.pojo.dto.Page;
import com.kuaizhan.controller.vo.JsonResponse;
import com.kuaizhan.controller.vo.PostListFlatVO;
import com.kuaizhan.controller.vo.PostListVO;
import com.kuaizhan.controller.vo.PostVO;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.PostService;
import com.kuaizhan.utils.JsonUtil;
import com.kuaizhan.utils.PojoSwitcher;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图文模块接口
 * Created by zixiong on 2017/3/20.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION, produces = AppConstant.PRODUCES)
public class PostController extends BaseController {

    @Resource
    private PostService postService;
    @Resource
    private AccountService accountService;

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);


    /**
     * 获取多图文列表
     */
    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public JsonResponse listPostByPagination(@RequestParam long weixinAppid, @RequestParam int page, @RequestParam(required = false) String title, @RequestParam(defaultValue = "0") Boolean flat) {

        Page<PostPO> postDOPage = postService.listPostsByPagination(weixinAppid, title, page, flat);
        List<PostPO> postPOList = postDOPage.getResult();

        // 展开多图文
        if (flat) {
            PostListFlatVO postListFlatVO = new PostListFlatVO();

            postListFlatVO.setTotalNum(postDOPage.getTotalCount());
            postListFlatVO.setCurrentPage(postDOPage.getPageNo());
            postListFlatVO.setTotalPage(postDOPage.getTotalPages());

            for (PostPO postPO : postPOList) {
                postListFlatVO.getPosts().add(PojoSwitcher.postPOToVO(postPO));
            }

            return new JsonResponse(postListFlatVO);

        // 不展开多图文
        } else {
            PostListVO postListVO = new PostListVO();

            postListVO.setTotalNum(postDOPage.getTotalCount());
            postListVO.setCurrentPage(postDOPage.getPageNo());
            postListVO.setTotalPage(postDOPage.getTotalPages());

            if (postPOList != null) {

                for (PostPO postPO : postPOList) {

                    // 多图文实体是一个list
                    List<PostVO> multiPostVOList = new ArrayList<>();

                    // 获取图文总记录下面的多图文
                    if (postPO.getType() == 2) {
                        List<PostPO> multiPostPOList = postService.listMultiPosts(weixinAppid, postPO.getMediaId(), false);
                        if (multiPostPOList != null) {
                            for (PostPO multiPostPO : multiPostPOList) {
                                PostVO multiPostVO = PojoSwitcher.postPOToVO(multiPostPO);
                                multiPostVOList.add(multiPostVO);
                            }
                            postListVO.getPosts().add(multiPostVOList);
                        } else {
                            // 发现垃圾数据
                            logger.error("[图文垃圾数据] 总记录下没有多图文, pageId:{}", postPO.getPageId());
                        }
                    // 单图文
                    } else {
                        PostVO postVO = PojoSwitcher.postPOToVO(postPO);

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
     */
    @RequestMapping(value = "/posts/{pageId}", method = RequestMethod.GET)
    public JsonResponse getPost(@PathVariable long pageId) {
        PostPO postPO = postService.getPostByPageId(pageId);
        PostVO postVO = PojoSwitcher.postPOToVO(postPO);
        return new JsonResponse(postVO);
    }

    /**
     * 获取多图文详情
     */
    @RequestMapping(value = "/multi_posts/{pageId}", method = RequestMethod.GET)
    public JsonResponse getMultiPost(@PathVariable long pageId) {
        PostPO postPO = postService.getPostByPageId(pageId);

        List<PostVO> multiPostVOList = new ArrayList<>();
        if (postPO != null) {
            // 如果图文type为3（多图文中的一条），根据mediaId找出多图文
            if (postPO.getType() == 3) {
                List<PostPO> multiPostPOList = postService.listMultiPosts(postPO.getWeixinAppid(), postPO.getMediaId(), true);
                for (PostPO multiPostPO : multiPostPOList) {
                    multiPostVOList.add(PojoSwitcher.postPOToVO(multiPostPO));
                }
            } else {
                multiPostVOList.add(PojoSwitcher.postPOToVO(postPO));
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
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 修改多图文
     */
    @RequestMapping(value = "/posts/{pageId}", method = RequestMethod.PUT)
    public JsonResponse updatePost(@PathVariable("pageId") long pageId, @Valid @RequestBody PostsParam postsParam) throws Exception {
        postService.updateMultiPosts(postsParam.getWeixinAppid(), pageId, postsParam.getPostDOs());
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 删除图文
     */
    @RequestMapping(value = "/posts/{pageId}", method = RequestMethod.DELETE)
    public JsonResponse deletePost(@RequestParam long weixinAppid, @PathVariable long pageId) {
        postService.deletePost(weixinAppid, pageId, accountService.getAccessToken(weixinAppid));
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 一键同步微信文章到快站微信
     */
    @RequestMapping(value = "/posts/wx_syncs", method = RequestMethod.POST)
    public JsonResponse wxSyncsPost(@Valid @RequestBody WxSyncsPostParam wxSyncsPostParam) throws ParamException {
        postService.syncWeixinPosts(wxSyncsPostParam.getWeixinAppid(), wxSyncsPostParam.getUid());
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 快站微信同步到快站文章
     */
    @RequestMapping(value = "/posts/kzweixin_syncs", method = RequestMethod.POST)
    public JsonResponse kzweixinSyncs2KzPost(@RequestBody String postData) throws  IOException {
        JSONObject jsonObject = new JSONObject(postData);
        Long weixinAppid = jsonObject.optLong("weixinAppid");
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        Long categoryId = jsonObject.optLong("categoryId");
        List<Long> pageIds = JsonUtil.string2List(jsonObject.get("pageIds").toString(), Long.class);
        for (Long pageId: pageIds){
            // TODO: 未绑定站点错误
            postService.export2KzArticle(pageId, categoryId, accountPO.getSiteId());
        }
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 从快站文章导入到快站微信
     */
    @RequestMapping(value = "/posts/kz_imports", method = RequestMethod.POST)
    public JsonResponse kzSyncsPost(@RequestBody String postData) throws IOException {
        JSONObject jsonObject = new JSONObject(postData);

        Long weixinAppid = jsonObject.optLong("weixinAppid");

        List<Long> pageIds = JsonUtil.string2List(jsonObject.get("pageIds").toString(), Long.class);
        postService.importKzArticle(weixinAppid, pageIds);
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 上传素材到微信服务器
     */
    @RequestMapping(value = "/weixin_materials", method = RequestMethod.POST)
    public JsonResponse uploadWeixinThumb(@Valid @RequestBody UploadPicParam uploadPicParam) throws DownloadFileFailedException {
        Map result = postService.uploadWxMaterial(uploadPicParam.getWeixinAppid(), uploadPicParam.getImgUrl());
        return new JsonResponse(result);
    }

    /**
     * 上传图文中图片
     */
    @RequestMapping(value = "weixin_pics", method = RequestMethod.POST)
    public JsonResponse uploadWeixinPic(@Valid @RequestBody UploadPicParam uploadPicParam) throws DownloadFileFailedException {
        String url = postService.uploadImageForPost(uploadPicParam.getWeixinAppid(), uploadPicParam.getImgUrl());
        Map<String ,String> result = new HashMap<>();
        result.put("url", url);
        return new JsonResponse(result);
    }

    /**
     * 获取某篇图文的微信链接
     */
    @RequestMapping(value = "/posts/{pageId}/wx_url", method = RequestMethod.GET)
    public JsonResponse getPostWxUrl(@PathVariable("pageId") long pageId, @RequestParam long weixinAppid) {
        Map<String ,String> result = new HashMap<>();
        result.put("wxUrl", postService.getPostWxUrl(weixinAppid, pageId));
        return new JsonResponse(result);
    }
}
