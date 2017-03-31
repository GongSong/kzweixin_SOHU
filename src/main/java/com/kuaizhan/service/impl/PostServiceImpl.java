package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApiConfig;
import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.config.MqConfig;
import com.kuaizhan.dao.mapper.PostDao;
import com.kuaizhan.exception.business.KZPostAddException;
import com.kuaizhan.exception.business.MaterialDeleteException;
import com.kuaizhan.dao.mongo.MongoPostDao;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.MongoException;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DO.MongoPostDo;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.ArticleDTO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.PostService;
import com.kuaizhan.service.WeixinPostService;
import com.kuaizhan.utils.HttpClientUtil;
import com.kuaizhan.utils.JsonUtil;
import com.kuaizhan.utils.MqUtil;
import com.kuaizhan.utils.IdGeneratorUtil;
import com.kuaizhan.utils.ReplaceCallbackMatcher;
import com.vdurmont.emoji.EmojiParser;
import javafx.geometry.Pos;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by zixiong on 2017/3/20.
 */
@Service("postService")
public class PostServiceImpl implements PostService {

    @Resource
    PostDao postDao;

    @Resource
    WeixinPostService weixinPostService;

    @Resource
    MqUtil mqUtil;

    @Resource
    MongoPostDao mongoPostDao;

    @Resource
    AccountService accountService;


    @Override
    public Page<PostDO> listPostsByPagination(long weixinAppid, String title, Integer page) throws DaoException {

        Page<PostDO> postDOPage = new Page<>(page, ApplicationConfig.PAGE_SIZE_LARGE);

        try {
            List<PostDO> posts = postDao.listPostsByPagination(weixinAppid, title, postDOPage);
            // 从Mongo中取content
            for (PostDO postDO : posts) {
                MongoPostDo mongoPostDo = mongoPostDao.getPostById(postDO.getPageId());
                // 多图文总记录没有content
                String content = (mongoPostDo != null) ? mongoPostDo.getContent() : "";
                postDO.setContent(content);
            }

            postDOPage.setResult(posts);

            long totalCount = postDao.count(weixinAppid, title);
            postDOPage.setTotalCount(totalCount);

        } catch (Exception e) {
            throw new DaoException(e);
        }

        return postDOPage;
    }

    @Override
    public List<PostDO> listMultiPosts(String mediaId) throws DaoException {

        List<PostDO> multiPosts;
        try {
            multiPosts = postDao.listMultiPosts(mediaId);

            // 从Mongo中取content
            for (PostDO postDO : multiPosts) {
                MongoPostDo mongoPostDo = mongoPostDao.getPostById(postDO.getPageId());
                // 多图文总记录没有content
                String content = (mongoPostDo != null) ? mongoPostDo.getContent() : "";
                postDO.setContent(content);
            }

        } catch (Exception e) {
            throw new DaoException(e);
        }
        return multiPosts;
    }

    @Override
    public void deletePost(long weixinAppid, long pageId, String accessToken) throws DaoException, MaterialDeleteException, MongoException {

        PostDO postDO = getPostByPageId(pageId);
        if (postDO != null) {
            String mediaId = postDO.getMediaId();
            //微信删除
            weixinPostService.deletePost(mediaId, accessToken);
            //数据库删除
            try {
                postDao.deletePost(weixinAppid, mediaId);
            } catch (Exception e) {
                throw new DaoException(e);
            }
        }
    }

    @Override
    public PostDO getPostByPageId(long pageId) throws DaoException, MongoException {
        PostDO postDO;
        try {

            postDO = postDao.getPost(pageId);
        } catch (Exception e) {
            throw new DaoException(e);
        }

        if (postDO != null) {
            // 获取content
            try {
                MongoPostDo mongoPostDo = mongoPostDao.getPostById(postDO.getPageId());
                String content = (mongoPostDo != null) ? mongoPostDo.getContent() : "";
                postDO.setContent(content);

            } catch (Exception e) {
                throw new MongoException(e);
            }
        }

        return postDO;
    }

    @Override
    public ArticleDTO getKzArticle(long pageId) throws IOException {
        String ret = HttpClientUtil.get(ApiConfig.kzArticleUrl(pageId));
        JSONObject jsonObject = new JSONObject(ret);
        ArticleDTO articleDTO = null;
        if (jsonObject.getInt("ret") == 0) {
            articleDTO = JsonUtil.string2Bean(jsonObject.get("data").toString(), ArticleDTO.class);
        }
        return articleDTO;
    }

    @Override
    public void importKzArticle(long weixinAppid, List<Long> pageIds) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("weixinAppid", weixinAppid);
        param.put("pageIds", pageIds);

        mqUtil.publish(MqConfig.IMPORT_KUAIZHAN_POST, param);
    }



    @Override
    public void export2KzArticle(long weixinAppid, long pageId, long categoryId, long siteId) throws DaoException, KZPostAddException, MongoException {
        //TODO:快文那边是否有去重判断？
        PostDO postDO = getPostByPageId(pageId);
        if (postDO != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("site_id", siteId);
            jsonObject.put("post_category_id", categoryId);
            jsonObject.put("post_title", postDO.getTitle());
            jsonObject.put("post_desc", postDO.getDigest());
            jsonObject.put("pic_url", postDO.getThumbUrl());
            jsonObject.put("post_content", postDO.getContent());
            String ret = HttpClientUtil.postJson(ApiConfig.kzPostArticleUrl(), jsonObject.toString());
            JSONObject returnJson = new JSONObject(ret);
            if (returnJson.getInt("ret") != 0) {
                throw new KZPostAddException();
            }
        } else {
            throw new KZPostAddException();
        }
    }

    @Override
    public void insertMultiPosts(long weixinAppid, List<PostDO> posts) throws Exception {

        AccountDO accountDO = accountService.getAccountByWeixinAppId(weixinAppid);
        // 在方法执行过程中，有失效的风险
        String accessToken = accountDO.getAccessToken();

        // 对图文数据做预处理
        posts = cleanPosts(posts, accessToken);

        // 上传到微信
        List<PostDO> wxPosts = new ArrayList<>();
        for (PostDO postDO : posts) {
            // 封装上传微信的PostDO list
            PostDO wxPost = new PostDO();
            wxPost.setTitle(postDO.getTitle());
            wxPost.setThumbMediaId(postDO.getThumbMediaId());
            wxPost.setAuthor(postDO.getAuthor());
            wxPost.setDigest(postDO.getDigest());
            wxPost.setShowCoverPic(postDO.getShowCoverPic());
            wxPost.setContentSourceUrl(postDO.getContentSourceUrl());

            // 替换内容
            String replacedContent = replaceContentForUpload(postDO.getContent());
            wxPost.setContent(replacedContent);
            wxPosts.add(wxPost);
        }
        String mediaId = weixinPostService.uploadPosts(accessToken, wxPosts);

        // 保存到数据库
        for (PostDO postDO: posts) {
            postDO.setMediaId(mediaId);
        }
        saveMultiPosts(weixinAppid, posts);
    }

    @Override
    public void updateMultiPosts(long weixinAppid, long pageId, List<PostDO> posts) throws Exception {

        PostDO oldPost = getPostByPageId(pageId);

        AccountDO accountDO = accountService.getAccountByWeixinAppId(weixinAppid);
        String accessToken = accountDO.getAccessToken();

        // 对图文数据做预处理
        posts = cleanPosts(posts, accessToken);

        // 单图文到单图文
        if (oldPost.getType() == 1 && posts.size() == 1){
            PostDO post = posts.get(0);

            // TODO: 考虑脏数据下，oldPost没有mediaId的异常处理
            // 更新到微信
            weixinPostService.updatePost(accessToken, oldPost.getMediaId(), post);

            // 更新到数据库

            // 数据修正
            post.setWeixinAppid(weixinAppid);
            post.setMediaId(oldPost.getMediaId());
            post.setIndex(0);
            post.setType((short) 1);

            postDao.updatePost(post, pageId);
        }
        // 单图文到多图文、多图文到单图文、多图文到多图文，执行删除微信media、删除本地、重新新增
        // 理由：单图文到多图文、多图文到单图文，都一定需要删除微信mediaId重新传，本地数据的存储变动也大(多图文总记录的更改)
        // 多图文到多图文，如果两个图文数相等，需要一次调用微信接口修改图文，如果不等，需要删除mediaId，成本高、逻辑也复杂
        else {
            // 删除微信
            // 删除本地
            // 新增
        }

    }

    /**
     * 对图文数据做预处理
     * 上传内容中的图片到微信、上传封面图片；替换emoji, 替换js
     * @param posts 多图文
     * @parm accessToken 上传用的token
     * @return
     */
    private List<PostDO> cleanPosts(List<PostDO> posts, String accessToken) throws Exception {

        for (PostDO postDO : posts) {
            // 上传图片
            String replacedContent = uploadContentImg(accessToken, postDO.getContent());
            // 过滤content中的js事件
            replacedContent = filterHtml(replacedContent);
            postDO.setContent(replacedContent);
            // 替换emoji
            postDO.setTitle(EmojiParser.removeAllEmojis(postDO.getTitle()));
            postDO.setDigest(EmojiParser.removeAllEmojis(postDO.getDigest()));
            // 上传封面图片
            String thumbMediaId = postDO.getMediaId();
            if (thumbMediaId == null || thumbMediaId.equals("")) {
                HashMap<String, String> retMap = weixinPostService.uploadImage(accessToken, postDO.getThumbUrl());
                thumbMediaId = retMap.get("mediaId");
            }
            postDO.setThumbMediaId(thumbMediaId);
        }

        return posts;
    }


    /**
     * 把校验后的多图文新增到数据库
     */
    private void saveMultiPosts(long weixinAppid, List<PostDO> posts) {

        // 1. 单图文 2. 多图文总记录 3. 多图文中的一条
        short type = (short) (posts.size() > 1 ? 3 : 1);
        int index = 0;
        String sumTitle = ""; // 把title拼接起来，保存多图文总记录里面
        for (PostDO postDO: posts){

            sumTitle += postDO.getTitle();

            // 先把content保存到mongo
            long pageId = IdGeneratorUtil.getID();
            MongoPostDo mongoPostDo = new MongoPostDo();
            mongoPostDo.setId(pageId);
            mongoPostDo.setContent(postDO.getContent());
            mongoPostDao.insertPost(mongoPostDo);

            postDO.setPageId(pageId);
            postDO.setWeixinAppid(weixinAppid);
            postDO.setType(type);
            postDO.setIndex(index++);

            postDao.insertPost(postDO);
        }

        // 如果是多图文，生成一条type为3的总数据
        if (posts.size() > 1) {

            PostDO sumPost = new PostDO();
            sumPost.setTitle(sumTitle);
            sumPost.setWeixinAppid(weixinAppid);
            sumPost.setThumbMediaId(posts.get(0).getThumbMediaId());
            sumPost.setThumbUrl(posts.get(0).getThumbUrl());
            sumPost.setShowCoverPic(posts.get(0).getShowCoverPic());
            sumPost.setAuthor(posts.get(0).getAuthor());
            sumPost.setDigest(posts.get(0).getDigest());
            sumPost.setContentSourceUrl(posts.get(0).getContentSourceUrl());
            sumPost.setMediaId(posts.get(0).getMediaId());
            sumPost.setPageId(posts.get(0).getPageId());
            sumPost.setType((short) 2);

            // 生成主键ID
            sumPost.setPageId(IdGeneratorUtil.getID());
            postDao.insertPost(sumPost);
        }
    }

    /**
     * 删除html中的js标签，on_click事件
     *
     * @param html
     * @return
     * @throws Exception
     */
    private String filterHtml(String html) throws Exception {
        // 删除js标签
        html = html.replaceAll("<script[^>]*?>.*?</script>", "");

        // 删除onclick等事件
        // 定义callback
        ReplaceCallbackMatcher.Callback callback = new ReplaceCallbackMatcher.Callback() {
            @Override
            public String getReplacement(Matcher matcher) throws Exception {
                // 去掉中间匹配到的onclick部分
                return matcher.group(1) + matcher.group(3);
            }
        };
        String regex = "(<\\S+?\\s+?)(on[a-z]+?=[\"'][^\"']*?[\"])([^>]*?>)";
        ReplaceCallbackMatcher callbackMatcher = new ReplaceCallbackMatcher(regex);
        html = callbackMatcher.replaceMatches(html, callback);

        return html;
    }

    /**
     * 上传图文content中的图片到微信，并写入wx_src到img标签
     */
    private String uploadContentImg(final String accessToken, String content) throws Exception {

        // 定义替换callback
        ReplaceCallbackMatcher.Callback callback = new ReplaceCallbackMatcher.Callback() {
            @Override
            public String getReplacement(Matcher matcher) throws Exception {
                String replacement;
                if (matcher.group().contains("wx_src")) {
                    // 存在wx_src则不替换
                    replacement = matcher.group();
                } else {
                    // 不存在wx_src，则从微信服务器换取wx_src，并加入到img标签中。
                    String src = matcher.group("src");
                    String wxSrc = weixinPostService.uploadImgForPost(accessToken, src);
                    replacement = matcher.group(1) + "\"" + src + "\" wx_src=\"" + wxSrc + "\"" + matcher.group(3);
                }
                return replacement;
            }
        };

        // 执行替换
        String regex = "(<img[^>]* src=)[\"'](?<src>[^\"']+)[\"']([^>]*>)";
        ReplaceCallbackMatcher callbackMatcher = new ReplaceCallbackMatcher(regex);
        content = callbackMatcher.replaceMatches(content, callback);

        return content;

    }

    /**
     * 按照微信的要求，对图文content做进一步处理
     *
     * @param content
     * @return
     */
    private String replaceContentForUpload(String content) throws Exception {
        // 用微信wx_src替换src

        // 定义callback
        ReplaceCallbackMatcher.Callback callback = new ReplaceCallbackMatcher.Callback() {
            @Override
            public String getReplacement(Matcher matcher) {
                return matcher.group(1) + matcher.group("wxSrc") + matcher.group(3) + matcher.group("wxSrc") + matcher.group(5);
            }
        };

        // wx_src在src前
        String regex = "(<img[^>]* src=\")(?<src>[^\"]+)(\"[^>]* wx_src=\")(?<wxSrc>[^\"]+)(\"[^>]*>)";
        ReplaceCallbackMatcher callbackMatcher = new ReplaceCallbackMatcher(regex);
        content = callbackMatcher.replaceMatches(content, callback);

        // wx_src在src后
        regex = "(<img[^>]* wx_src=\")(?<wxSrc>[^\"]+)(\"[^>]* src=\")(?<src>[^\"]+)(\"[^>]*>)";
        callbackMatcher = new ReplaceCallbackMatcher(regex);
        content = callbackMatcher.replaceMatches(content, callback);

        // TODO: 对中转链接的替换； 校验上传成功的微信图片，肯定是小于1M的。
        return content;
    }
}
