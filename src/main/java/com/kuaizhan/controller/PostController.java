package com.kuaizhan.controller;

import com.kuaizhan.annotation.Validate;
import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.exception.business.AccountNotExistException;
import com.kuaizhan.exception.business.KZPostAddException;
import com.kuaizhan.exception.business.MaterialDeleteException;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.JsonParseException;
import com.kuaizhan.exception.system.MongoException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.pojo.VO.JsonResponse;
import com.kuaizhan.pojo.VO.PostListVO;
import com.kuaizhan.pojo.VO.PostVO;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.PostService;
import com.kuaizhan.utils.JsonUtil;
import com.kuaizhan.utils.PojoSwitcher;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 图文消息controller
 * Created by zixiong on 2017/3/20.
 */
@RestController
@RequestMapping(value = ApplicationConfig.VERSION, produces = "application/json")
public class PostController extends BaseController {

    @Resource
    PostService postService;
    @Resource
    AccountService accountService;
    @Resource
    PojoSwitcher pojoSwitcher;



    /**
     * 获取多图文列表
     * @throws DaoException
     */

    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public JsonResponse listPostByPagination(@RequestParam long weixinAppid, @RequestParam int page) throws DaoException {

        Page<PostDO> postDOPage = postService.listPostsByPagination(weixinAppid, page);

        List<PostDO> postDOList = postDOPage.getResult();

        PostListVO postListVO = new PostListVO();

        if (postDOList != null) {
            postListVO.setTotalNum(postDOPage.getTotalCount());
            postListVO.setCurrentPage(postDOPage.getPageNo());
            postListVO.setTotalPage(postDOPage.getTotalPages());

            for (PostDO postDO : postDOList) {

                // 多图文实体是一个list
                List<PostVO>  multiPostVOList = new ArrayList<>();

                // 获取图文总记录下面的多图文
                if (postDO.getType() == 2) {
                    List<PostDO> multiPostDOList = postService.listMultiPosts(postDO.getMediaId());
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
     * @return
     */
    @RequestMapping(value = "/multi_posts/{pageId}", method = RequestMethod.GET)
    public JsonResponse getMultiPost(@RequestParam long weixinAppid, @PathVariable long pageId) throws DaoException, MongoException {
        PostDO postDO = postService.getPostByPageId(pageId);

        List<PostVO> multiPostVOList = new ArrayList<>();
        if (postDO != null) {
            // 如果图文type为3（多图文中的一条），根据mediaId找出多图文
            if (postDO.getType() == 3) {
                List<PostDO> multiPostDOList = postService.listMultiPosts(postDO.getMediaId());

                for (PostDO multipostDO: multiPostDOList) {

                    multiPostVOList.add(pojoSwitcher.postDOToVO(multipostDO));
                }
            } else {
                multiPostVOList.add(pojoSwitcher.postDOToVO(postDO));
            }
        }
        return new JsonResponse(multiPostVOList);
    }

    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    public JsonResponse insertPost(@RequestParam long weixinAppid) {
        return new JsonResponse(null);
    }

    @RequestMapping(value = "/posts", method = RequestMethod.PUT)
    public JsonResponse updatePost(@RequestParam long weixinAppid) {
        return new JsonResponse(null);
    }

    @RequestMapping(value = "/posts/{pageId}", method = RequestMethod.DELETE)
    public JsonResponse deletePost(@RequestParam long weixinAppid, @PathVariable long pageId) throws RedisException, DaoException, AccountNotExistException, MaterialDeleteException, JsonParseException, MongoException {
        AccountDO accountDO = accountService.getAccountByWeixinAppId(weixinAppid);
        postService.deletePost(weixinAppid, pageId, accountDO.getAccessToken());
        return new JsonResponse(null);
    }

    /**
     * 微信文章同步到快站
     *
     * @param weixinAppid
     * @return
     */
    @RequestMapping(value = "/posts/wx_syncs", method = RequestMethod.POST)
    public JsonResponse wxSyncsPost(@RequestParam long weixinAppid) {
        return new JsonResponse(null);
    }

    /**
     * 快站微信同步到快站文章
     *
     * @param weixinAppid
     * @return
     */
    @RequestMapping(value = "/posts/kzweixin_syncs", method = RequestMethod.POST)
    public JsonResponse kzweixinSyncs2KzPost(@Validate(key = "weixinAppid") @RequestParam long weixinAppid, @Validate(key = "postData", path = ApplicationConfig.POST_KZWEIXINSYNCS2KZPOST_POSTDATAT_SCHEMA) @RequestBody String postData) throws KZPostAddException, DaoException, MongoException {
        JSONObject jsonObject = new JSONObject(postData);
        long siteId = jsonObject.getLong("siteId");
        long pageId = jsonObject.getLong("pageId");
        long categoryId = jsonObject.getLong("categoryId");
        postService.export2KzArticle(weixinAppid, pageId, categoryId, siteId);
        return new JsonResponse(null);
    }


    /**
     * 快站文章同步到微信
     *
     * @param weixinAppid
     * @param postData
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/posts/kz_syncs", method = RequestMethod.POST)
    public JsonResponse kzSyncsPost(@Validate(key = "weixinAppid") @RequestParam long weixinAppid, @Validate(key = "postData",path = ApplicationConfig.POST_KZSYNCS_POSTDATAT_SCHEMA) @RequestBody String postData) throws IOException {
        JSONObject jsonObject = new JSONObject(postData);
        List<Long> pageIds = JsonUtil.string2List(jsonObject.get("pageIds").toString(), Long.class);
        postService.importKzArticle(weixinAppid, pageIds);
        return new JsonResponse(null);
    }
}
