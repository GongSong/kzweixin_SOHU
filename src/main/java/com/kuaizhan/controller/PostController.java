package com.kuaizhan.controller;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.exception.business.*;
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
import com.kuaizhan.service.WeixinPostService;
import com.kuaizhan.utils.JsonUtil;
import com.kuaizhan.utils.PojoSwitcher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Resource
    WeixinPostService weixinPostService;


    /**
     * 获取多图文列表
     *
     * @throws DaoException
     */

    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public JsonResponse listPostByPagination(@RequestParam long weixinAppid, @RequestParam int page, @RequestParam(required = false) String title) throws DaoException {

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
                List<PostDO> multiPostDOList = postService.listMultiPosts(postDO.getMediaId());

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
    public JsonResponse insertPost(@RequestBody String postData) throws Exception {
        JSONObject jsonObject = new JSONObject(postData);

        Long weixinAppid = jsonObject.optLong("weixinAppid");

        JSONArray jsonArray = jsonObject.getJSONArray("posts");
        List<PostDO> posts = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject postJson = jsonArray.getJSONObject(i);
            PostDO postDO = new PostDO();
            postDO.setTitle(postJson.getString("title"));
            postDO.setAuthor(postJson.optString("author"));
            postDO.setDigest(postJson.getString("digest"));
            postDO.setContent(postJson.getString("content"));
            postDO.setThumbMediaId(postJson.optString("thumbMediaId"));
            postDO.setThumbUrl(postJson.getString("thumbUrl"));
            postDO.setContentSourceUrl(postJson.optString("contentSourceUrl"));
            postDO.setShowCoverPic((short) postJson.optInt("showCoverPic", 0));

            posts.add(postDO);
        }
        postService.insertMultiPosts(weixinAppid, posts);

        return new JsonResponse(null);
    }

    /**
     * 修改多图文
     */
    @RequestMapping(value = "/posts/{pageId}", method = RequestMethod.PUT)
    public JsonResponse updatePost(@PathVariable("pageId") long pageId, @RequestBody String postData) throws Exception {
        JSONObject jsonObject = new JSONObject(postData);

        Long weixinAppid = jsonObject.optLong("weixinAppid");

        JSONArray jsonArray = jsonObject.getJSONArray("posts");
        List<PostDO> posts = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject postJson = jsonArray.getJSONObject(i);
            PostDO postDO = new PostDO();

            postDO.setPageId(postJson.optLong("pageId"));
            postDO.setMediaId(postJson.optString("mediaId"));
            postDO.setTitle(postJson.getString("title"));
            postDO.setAuthor(postJson.optString("author"));
            postDO.setDigest(postJson.getString("digest"));
            postDO.setContent(postJson.getString("content"));
            postDO.setThumbMediaId(postJson.optString("thumbMediaId"));
            postDO.setThumbUrl(postJson.getString("thumbUrl"));
            postDO.setContentSourceUrl(postJson.optString("contentSourceUrl"));
            postDO.setShowCoverPic((short) postJson.optInt("showCoverPic", 0));

            posts.add(postDO);
        }
        postService.updateMultiPosts(weixinAppid, pageId, posts);
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
        AccountDO accountDO = accountService.getAccountByWeixinAppId(weixinAppid);
        postService.deletePost(weixinAppid, pageId, accountDO.getAccessToken());
        return new JsonResponse(null);
    }

    /**
     * 一键同步微信文章到快站微信
     *
     * @return
     */
    @RequestMapping(value = "/posts/wx_syncs", method = RequestMethod.POST)
    public JsonResponse wxSyncsPost(@RequestBody String postData) throws ParamException {
        JSONObject jsonObject = new JSONObject(postData);

        Long weixinAppid = jsonObject.optLong("weixinAppid");
        long uid = jsonObject.optLong("uid");
        if (uid == 0 || weixinAppid == 0){
            throw new ParamException("uid和weixinAppid是必传参数");
        }
        postService.syncWeixinPosts(weixinAppid, uid);
        return new JsonResponse(null);
    }


    /**
     * 快站微信同步到快站文章
     *
     * @return
     */
    @RequestMapping(value = "/posts/kzweixin_syncs", method = RequestMethod.POST)
    public JsonResponse kzweixinSyncs2KzPost(@RequestBody String postData) throws KZPostAddException, DaoException, MongoException, IOException {

        JSONObject jsonObject = new JSONObject(postData);

        Long weixinAppid = jsonObject.optLong("weixinAppid");
        Long siteId = jsonObject.optLong("siteId");
        Long categoryId = jsonObject.optLong("categoryId");
        List<Long> pageIds = JsonUtil.string2List(jsonObject.get("pageIds").toString(), Long.class);

        for (Long pageId: pageIds){
            postService.export2KzArticle(weixinAppid, pageId, categoryId, siteId);
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
            JsonParseException, DaoException, AccountNotExistException, AddMaterialException {
        JSONObject jsonObject = new JSONObject(postData);
        Long weixinAppid = jsonObject.optLong("weixinAppid");
        String imgUrl = jsonObject.optString("imgUrl");
        if (weixinAppid == 0 || imgUrl == null){
            throw new ParamException();
        }
        AccountDO accountDO = accountService.getAccountByWeixinAppId(weixinAppid);
        Map result = weixinPostService.uploadImage(accountDO.getAccessToken(), imgUrl);

        return new JsonResponse(result);
    }

}
