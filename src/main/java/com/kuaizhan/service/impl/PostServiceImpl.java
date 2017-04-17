package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApiConfig;
import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.config.MqConfig;
import com.kuaizhan.dao.mapper.PostDao;
import com.kuaizhan.exception.business.*;
import com.kuaizhan.dao.mongo.MongoPostDao;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.JsonParseException;
import com.kuaizhan.exception.system.MongoException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.ArticleDTO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.pojo.DTO.PostDTO;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.KZPicService;
import com.kuaizhan.service.PostService;
import com.kuaizhan.service.WeixinPostService;
import com.kuaizhan.utils.*;
import com.vdurmont.emoji.EmojiParser;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;

/**
 * Created by zixiong on 2017/3/20.
 */
@Service("postService")
public class PostServiceImpl implements PostService {

    private static final Logger logger = Logger.getLogger(PostServiceImpl.class);

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

    @Resource
    KZPicService kZPicService;


    @Override
    public Page<PostDO> listPostsByPagination(long weixinAppid, String title, Integer page) throws DaoException, MongoException {

        Page<PostDO> postDOPage = new Page<>(page, ApplicationConfig.PAGE_SIZE_LARGE);

        List<PostDO> posts;
        try {
            posts = postDao.listPostsByPagination(weixinAppid, title, postDOPage);
        } catch (Exception e) {
            throw new DaoException(e);
        }

        try {
            // 从Mongo中取content
            for (PostDO postDO : posts) {
                postDO.setContent(mongoPostDao.getContentById(postDO.getPageId()));
            }
        } catch (Exception e) {
            throw new MongoException(e);
        }

        long totalCount = postDao.count(weixinAppid, title);
        postDOPage.setTotalCount(totalCount);
        postDOPage.setResult(posts);

        return postDOPage;
    }

    @Override
    public List<PostDO> listMultiPosts(String mediaId) throws DaoException, MongoException {

        List<PostDO> multiPosts;
        try {
            multiPosts = postDao.listMultiPosts(mediaId);
        } catch (Exception e) {
            throw new DaoException(e);
        }

        try {
            // 从Mongo中取content
            for (PostDO postDO : multiPosts) {
                postDO.setContent(mongoPostDao.getContentById(postDO.getPageId()));
            }
        } catch (Exception e) {
            throw new MongoException(e);
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
            //mongo删除
            try {
                mongoPostDao.deletePost(postDO.getPageId());
            } catch (Exception e) {
                throw new MongoException(e);
            }
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
                postDO.setContent(mongoPostDao.getContentById(postDO.getPageId()));
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
    public void export2KzArticle(long pageId, long categoryId, long siteId) throws DaoException, KZPostAddException, MongoException {
        PostDO postDO = getPostByPageId(pageId);
        if (postDO != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("site_id", siteId);
            jsonObject.put("post_category_id", categoryId);
            jsonObject.put("post_title", postDO.getTitle());
            jsonObject.put("post_desc", postDO.getDigest());
            jsonObject.put("pic_url", postDO.getThumbUrl());
            try {
                jsonObject.put("post_content", mongoPostDao.getContentById(pageId));
            } catch (Exception e) {
                throw new MongoException(e);
            }
            String ret = HttpClientUtil.postJson(ApiConfig.kzPostArticleUrl(), jsonObject.toString());
            JSONObject returnJson = new JSONObject(ret);
            if (returnJson.getInt("ret") != 0) {
                logger.error("[同步到快站文章失败]param:" + jsonObject + "return: " + returnJson);
                throw new KZPostAddException();
            }
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
        String mediaId = weixinPostService.uploadPosts(accessToken, wrapWeiXinPosts(accessToken, posts));

        // 保存到数据库
        for (PostDO postDO : posts) {
            postDO.setMediaId(mediaId);
        }
        saveMultiPosts(weixinAppid, posts);
    }

    @Override
    public void updateMultiPosts(long weixinAppid, long pageId, List<PostDO> posts) throws Exception {

        PostDO oldPost = getPostByPageId(pageId);
        if (oldPost == null) {
            throw new PostNotExistException();
        }

        AccountDO accountDO = accountService.getAccountByWeixinAppId(weixinAppid);
        String accessToken = accountDO.getAccessToken();

        // 对图文数据做预处理
        posts = cleanPosts(posts, accessToken);

        // 单图文到单图文 ==>  更新到微信、更新到数据库
        if (oldPost.getType() == 1 && posts.size() == 1) {
            PostDO post = posts.get(0);

            // 更新到微信
            weixinPostService.updatePost(accessToken, oldPost.getMediaId(), post);

            // 更新到数据库

            // 数据修正
            post.setWeixinAppid(weixinAppid);
            post.setMediaId(oldPost.getMediaId());
            post.setIndex(0);
            post.setType((short) 1);

            mongoPostDao.upsertPost(pageId, post.getContent());
            postDao.updatePost(post, pageId);
        }
        // 单图文到多图文、多图文到单图文、多图文到多图文，执行删除微信media、删除本地、重新新增
        // 理由：单图文到多图文、多图文到单图文，都一定需要删除微信mediaId重新传，本地数据的存储变动也大(多图文总记录的更改)
        // 多图文到多图文，如果两个图文数相等，需要一次调用微信接口修改图文，如果不等，需要删除mediaId，成本高、逻辑也复杂
        else {
            // 上传新的图文到微信, 先新增到微信，下面操作失败时，避免丢失数据
            String mediaId = weixinPostService.uploadPosts(accessToken, wrapWeiXinPosts(accessToken, posts));
            for (PostDO postDO : posts) {
                postDO.setMediaId(mediaId);
            }

            // 删除老图文
            deletePost(weixinAppid, pageId, accessToken);

            // 重新新增
            saveMultiPosts(weixinAppid, posts);
        }

    }

    /**
     * 把存数据库的多图文DO对象，封装成同步给微信的。
     * 替换了内容中的图片url为wx_src
     */
    private List<PostDO> wrapWeiXinPosts(String accessToken, List<PostDO> posts) throws Exception {
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
            String replacedContent = replaceContentForUpload(accessToken, postDO.getContent());
            wxPost.setContent(replacedContent);
            wxPosts.add(wxPost);
        }
        return wxPosts;
    }

    /**
     * 对图文数据做预处理
     * 上传内容中的图片到微信、上传封面图片；替换emoji, 替换js
     *
     * @param posts 多图文
     * @return
     * @parm accessToken 上传用的token
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
            String digest = postDO.getDigest();
            if (digest != null) {
                digest = EmojiParser.removeAllEmojis(digest);
            }
            postDO.setDigest(digest);
            // 上传封面图片
            String thumbMediaId = postDO.getThumbMediaId();
            if (thumbMediaId == null || thumbMediaId.equals("")) {
                HashMap<String, String> retMap = weixinPostService.uploadImage(accessToken, postDO.getThumbUrl());
                thumbMediaId = retMap.get("mediaId");
            }
            // TODO: 如果没有content_source_url，设置为快站分享的页面url
            postDO.setThumbMediaId(thumbMediaId);

            // 清除封面尺寸---"/imageView/v1/thumbnail"
            postDO.setThumbUrl(postDO.getThumbUrl().replaceAll("/imageView/v1/thumbnail.*$", ""));
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
        StringBuilder sumTitle = new StringBuilder(); // 把title拼接起来，保存多图文总记录里面
        for (PostDO postDO : posts) {

            sumTitle.append(postDO.getTitle());

            // 没有指定pageId, 则生成
            if (postDO.getPageId() == null || postDO.getPageId() == 0) {
                postDO.setPageId(IdGeneratorUtil.getID());
            }
            // 先把content保存到mongo
            mongoPostDao.upsertPost(postDO.getPageId(), postDO.getContent());

            postDO.setWeixinAppid(weixinAppid);
            postDO.setType(type);
            postDO.setIndex(index++);

            postDao.insertPost(postDO);
        }

        // 如果是多图文，生成一条type为2的图文总记录
        if (posts.size() > 1) {

            PostDO sumPost = new PostDO();
            // 生成pageId
            sumPost.setPageId(IdGeneratorUtil.getID());
            // 多图文根据mediaId表示是一组图文
            sumPost.setMediaId(posts.get(0).getMediaId());
            sumPost.setWeixinAppid(weixinAppid);
            sumPost.setTitle(sumTitle.toString());
            sumPost.setThumbMediaId(posts.get(0).getThumbMediaId());
            sumPost.setThumbUrl(posts.get(0).getThumbUrl());
            sumPost.setShowCoverPic(posts.get(0).getShowCoverPic());
            sumPost.setAuthor(posts.get(0).getAuthor());
            sumPost.setDigest(posts.get(0).getDigest());
            sumPost.setContentSourceUrl(posts.get(0).getContentSourceUrl());
            sumPost.setType((short) 2);

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
    private String replaceContentForUpload(final String accessToken, String content) throws Exception {
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

        // 替换background-image的callback
        callback = new ReplaceCallbackMatcher.Callback() {
            @Override
            public String getReplacement(Matcher matcher) throws Exception {
                return matcher.group(1) + weixinPostService.uploadImgForPost(accessToken, matcher.group(2)) + matcher.group(3);
            }
        };
        regex = "(background-image: url\\()([^\\)]+)(\\))";
        callbackMatcher = new ReplaceCallbackMatcher(regex);
        content = callbackMatcher.replaceMatches(content, callback);

        // TODO: 对中转链接的替换； 校验上传成功的微信图片，肯定是小于1M的。
        return content;
    }

    @Override
    public List<String> listMediaIdsByWeixinAppid(long weixinAppid) throws DaoException {
        try {
            return postDao.listMediaIdsByWeixinAppid(weixinAppid);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void syncWeixinPosts(long weixinAppid, long uid) {
        Map<String, Object> map = new HashMap<>();
        map.put("weixinAppid", weixinAppid);
        map.put("uid", uid);
        mqUtil.publish(MqConfig.IMPORT_WEIXIN_POST_LIST, map);
    }

    @Override
    public void importWeixinPost(PostDTO.PostItem postItem, long userId) throws Exception {
        // 将微信返回的文章处理为数据库存储的图文
        List<PostDO> postDOList = new LinkedList<>();
        List<PostDTO.Item.Content.NewsItem> newsItems = postItem.getItem().getContent().getNewsItems();
        int key = 0;
        for (PostDTO.Item.Content.NewsItem newsItem : newsItems) {
            PostDO postDO = new PostDO();
            // 标题去除emoji
            postDO.setTitle(EmojiParser.removeAllEmojis(newsItem.getTitle()));
            // 摘要去除emoji
            postDO.setDigest(EmojiParser.removeAllEmojis(newsItem.getDigest()));
            // 内容替换图片及视频
            String content = replacePicUrlFromWeixinPost(newsItem.getContent(), userId);
            postDO.setContent(replaceVideoFromWeixinPost(content));
            // 缩略图mediaId
            postDO.setThumbMediaId(newsItem.getThumbMediaId());
            // 缩略图链接替换
            String picUrl;
            if (newsItem.getThumbUrl() == null || "".equals(newsItem.getThumbUrl())) {
                picUrl = (key == 0) ? getFirstPostDefaultThumbUrl() : getCommonPostDefaultThumbUrl();
            } else {
                picUrl = getKzImgUrlByWeixinImgUrl(newsItem.getThumbUrl(), userId);
            }
            postDO.setThumbUrl(picUrl);
            // 是否显示封面
            postDO.setShowCoverPic(newsItem.getShowCoverPic());
            // 作者
            postDO.setAuthor(newsItem.getAuthor());
            // 图文消息原文链接
            postDO.setContentSourceUrl(newsItem.getContentSourceUrl() == null ? "" : newsItem.getContentSourceUrl());
            // 图文mediaId
            postDO.setMediaId(postItem.getItem().getMediaId());
            // 更新时间
            postDO.setUpdateTime(DateUtil.timestampSec());
            // 同步时间
            postDO.setSyncTime(DateUtil.timestampSec());
            // 状态
            postDO.setStatus((short) 1);

            postDOList.add(postDO);

            ++key;
        }

        // 入库
        saveMultiPosts(postItem.getWeixinAppid(), postDOList);
    }

    @Override
    public List<PostDTO.PostItem> listNonExistsPostItemsFromWeixin(long weixinAppid) throws DaoException, AccountNotExistException, RedisException, JsonParseException, MaterialGetException {
        List<PostDTO.PostItem> differPostItems = new LinkedList<>();

        // 获取所有微信图文
        List<PostDTO> postDTOList = weixinPostService.listAllPosts(accountService.getAccountByWeixinAppId(weixinAppid).getAccessToken());

        if (postDTOList == null || postDTOList.size() <= 0) {
            return differPostItems;
        }

        List<PostDTO.PostItem> postItemList = new LinkedList<>();
        for (PostDTO postDTO : postDTOList) {
            postItemList.addAll(postDTO.toPostItemList(weixinAppid));
        }

        // 获取本地所有微信图文
        List<String> mediaIds = listMediaIdsByWeixinAppid(weixinAppid);
        Set<String> mediaIdsSet = new HashSet<>();

        mediaIdsSet.addAll(mediaIds);

        // 对比差异
        for (PostDTO.PostItem postItem : postItemList) {
            if (!mediaIdsSet.contains(postItem.getItem().getMediaId())) {
                differPostItems.add(postItem);
            }
        }

        return differPostItems;
    }

    /**
     * 去除某字符串前缀
     *
     * @param str
     * @param prefix
     * @return
     */
    private String removeUrlPrefixIfExists(String str, String prefix) {
        if (str == null || prefix == null) return str;

        if (str.contains(prefix)) {
            str = str.substring(str.lastIndexOf(prefix) + prefix.length());
            try {
                str = URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return str;
            }
        }

        return str;
    }

    /**
     * 去除某字符串后缀
     *
     * @param str
     * @param suffix
     * @return
     */
    private String removeUrlSuffixIfExists(String str, String suffix) {
        if (str == null || suffix == null) return str;

        if (str.contains(suffix)) {
            str = str.substring(0, str.lastIndexOf(suffix));
            try {
                str = URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return str;
            }
        }

        return str;
    }

    /**
     * 微信图片地址生成快站图片url，若失败则返回原url
     *
     * @param imgUrl
     * @return
     */
    private String getKzImgUrlByWeixinImgUrl(String imgUrl, long userId) {
        // 若不是微信图片则返回原url
        if (!imgUrl.contains("mmbiz")) return imgUrl;

        // 去除脚本造成的老数据
        imgUrl = removeUrlPrefixIfExists(imgUrl, "url=");

        // 去除pic-redirect前缀
        imgUrl = removeUrlPrefixIfExists(imgUrl, "pic-redirect?url=");
        imgUrl = removeUrlPrefixIfExists(imgUrl, "pic-redirect.php?url=");

        // 去除微信某些图片的后缀，否则getimagesize失败
        imgUrl = removeUrlSuffixIfExists(imgUrl, "&tp=webp");
        imgUrl = removeUrlSuffixIfExists(imgUrl, "?tp=webp");
        imgUrl = removeUrlPrefixIfExists(imgUrl, "&amp;");

        String kzPicUrl = imgUrl;

        try {
            kzPicUrl = kZPicService.uploadByUrlAndUserId(imgUrl, userId);
        } catch (KZPicUploadException e) {
            LogUtil.logMsg(e);
        }

        return kzPicUrl;
    }

    /**
     * 替换微信内容中的微信图片链接为快站链接
     *
     * @param content
     * @return
     */
    private String replacePicUrlFromWeixinPost(String content, final long userId) throws Exception {
        // 将文章中的图片的data-src替换为src
        content = content.replaceAll("(<img [^>]*)(data-src)", "$1src");

        // img中的微信图片转成快站链接,记录wx_src
        ReplaceCallbackMatcher.Callback callback = new ReplaceCallbackMatcher.Callback() {
            @Override
            public String getReplacement(Matcher matcher) throws Exception {
                return matcher.group(1)
                        + getKzImgUrlByWeixinImgUrl(matcher.group(2), userId)
                        + "\" wx_src=\""
                        + matcher.group(2)
                        + matcher.group(3)
                        ;
            }
        };
        String regex = "(<img[^>]* src=\")([^\"]+)(\"[^>]*>)";
        ReplaceCallbackMatcher replaceCallbackMatcher = new ReplaceCallbackMatcher(regex);
        content = replaceCallbackMatcher.replaceMatches(content, callback);

        callback = new ReplaceCallbackMatcher.Callback() {
            @Override
            public String getReplacement(Matcher matcher) throws Exception {
                return matcher.group(1)
                        + getKzImgUrlByWeixinImgUrl(matcher.group(2), userId)
                        + "' wx_src='"
                        + matcher.group(2)
                        + matcher.group(3)
                        ;
            }
        };
        regex = "(<img[^>]* src=')([^']+)('[^>]*>)";
        replaceCallbackMatcher = new ReplaceCallbackMatcher(regex);
        content = replaceCallbackMatcher.replaceMatches(content, callback);

        // 背景图中的微信图片转成快站链接
        callback = new ReplaceCallbackMatcher.Callback() {
            @Override
            public String getReplacement(Matcher matcher) throws Exception {
                return "(" + getKzImgUrlByWeixinImgUrl(matcher.group(1), userId) + ")";
            }
        };
        regex = "\\((https?:\\/\\/mmbiz[^)]+)\\)";
        replaceCallbackMatcher = new ReplaceCallbackMatcher(regex);
        replaceCallbackMatcher.replaceMatches(content, callback);

        return content;
    }

    /**
     * 替换微信图文中视频链接
     *
     * @param content
     * @return
     */
    private String replaceVideoFromWeixinPost(String content) {
        return content.replaceAll(
                "<iframe[^>]*class=\"video_iframe\"[^>]*data-src=\"([^\"]+)vid=([^&]+)([^\"]+)\"[^>]*>",
                "<iframe style=\"z-index:1;\" class=\"video_iframe\" data-src=\"$1vid=$2$3\" frameborder=\"0\" allowfullscreen=\"\" src=\"https://v.qq.com/iframe/player.html?vid=$2&tiny=0&auto=0\" width=\"280px\" height=\"100%\">"
        );
    }

    /**
     * 多图文第一篇图文的默认封面图
     *
     * @return
     */
    private String getFirstPostDefaultThumbUrl() {
        return ApplicationConfig.getResUrl("/res/weixin/images/post-default-cover-900-500.png");
    }

    /**
     * 多图文非第一篇图文的默认封面图
     *
     * @return
     */
    private String getCommonPostDefaultThumbUrl() {
        return ApplicationConfig.getResUrl("/res/weixin/images/post-default-cover-200-200.png");
    }

}
