package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.config.KzApiConfig;
import com.kuaizhan.constant.MqConstant;
import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.dao.mapper.PostDao;
import com.kuaizhan.dao.redis.RedisPostDao;
import com.kuaizhan.exception.business.*;
import com.kuaizhan.dao.mongo.MongoPostDao;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.JsonParseException;
import com.kuaizhan.exception.system.MongoException;
import com.kuaizhan.exception.system.RedisException;
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
    RedisPostDao redisPostDao;

    @Resource
    AccountService accountService;

    @Resource
    KZPicService kZPicService;


    @Override
    public Page<PostDO> listPostsByPagination(long weixinAppid, String title, Integer page, Boolean flat) throws DaoException {

        Page<PostDO> postDOPage = new Page<>(page, AppConstant.PAGE_SIZE_MIDDLE);

        List<PostDO> posts;
        try {
            posts = postDao.listPostsByPagination(weixinAppid, title, postDOPage, flat);
        } catch (Exception e) {
            throw new DaoException(e);
        }

        long totalCount = postDao.count(weixinAppid, title, flat);
        postDOPage.setTotalCount(totalCount);
        postDOPage.setResult(posts);

        return postDOPage;
    }

    @Override
    public List<PostDO> listMultiPosts(long weixinAppid, String mediaId, Boolean withContent) throws DaoException, MongoException {

        List<PostDO> multiPosts;
        try {
            multiPosts = postDao.listMultiPosts(weixinAppid, mediaId);
        } catch (Exception e) {
            throw new DaoException(e);
        }

        // 从Mongo中取content
        if (withContent) {
            for (PostDO postDO : multiPosts) {
                postDO.setContent(getPostContent(postDO.getPageId()));
            }
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
    public Boolean exist(long weixinAppid, String mediaId) throws DaoException {
        try {
            return postDao.exist(weixinAppid, mediaId);
        } catch (Exception e){
            throw new DaoException(e);
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
            postDO.setContent(getPostContent(pageId));
        }

        return postDO;
    }

    @Override
    public String getPostContent(long pageId) {
        // 先从mongo中获取
        String content;
        content = mongoPostDao.getContentById(pageId);

        // 从mongo中获取不到，则从接口中获取，并写入mongo
        if (content == null) {
            Map<String, String> headers = new HashMap<>();
            headers.put("Host", ApplicationConfig.KZ_SERVICE_HOST);
            String result = HttpClientUtil.get(KzApiConfig.getKzArticleContentUrl(pageId), headers);
            if (result != null && result.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(result);
                content = jsonObject.getJSONObject("data").optString("content");

                // 存入本地mongo
                if (content != null && ! "".equals(content)) {
                    logger.info("从快站接口获取内容成功, pageId: " + pageId);
                    mongoPostDao.upsertPost(pageId, content);
                }
            }
        }
        return content;
    }

    @Override
    public ArticleDTO getKzArticle(long pageId) throws IOException {

        String ret = HttpClientUtil.get(KzApiConfig.getKzArticleUrl(pageId));
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

        mqUtil.publish(MqConstant.IMPORT_KUAIZHAN_POST, param);
    }


    @Override
    public void export2KzArticle(long pageId, long categoryId, long siteId) throws DaoException, KZPostAddException, MongoException {
        PostDO postDO = getPostByPageId(pageId);
        if (postDO != null) {
            Map<String,Object> param=new HashMap<>();
            param.put("site_id", siteId);
            param.put("post_category_id", categoryId);
            param.put("post_title", postDO.getTitle());
            param.put("post_desc", postDO.getDigest());
            param.put("pic_url", UrlUtil.fixProtocol(postDO.getThumbUrl()));
            param.put("post_content", getPostContent(pageId));

            String ret = HttpClientUtil.post(KzApiConfig.KZ_POST_ARTICLE_URL, param);
            JSONObject returnJson = new JSONObject(ret);
            if (returnJson.getInt("ret") != 0) {
                logger.error("[同步到快站文章失败] pageId: " + pageId + "return: " + returnJson + " param:" + param);
                throw new KZPostAddException();
            }
        }
    }

    @Override
    public void insertMultiPosts(long weixinAppid, List<PostDO> posts) throws Exception {

        // 在方法执行过程中，有失效的风险
        String accessToken = accountService.getAccessToken(weixinAppid);

        // 对图文数据做预处理
        posts = cleanPosts(posts, accessToken);

        // 上传到微信
        String mediaId = weixinPostService.uploadPosts(accessToken, wrapWeiXinPosts(accessToken, posts));
        for (PostDO postDO : posts) {
            postDO.setMediaId(mediaId);
        }

        // 入库
        saveMultiPosts(weixinAppid, posts);
    }

    @Override
    public void updateMultiPosts(long weixinAppid, long pageId, List<PostDO> posts) throws Exception {

        // 老图文
        List<PostDO> oldPosts;
        PostDO oldPost = getPostByPageId(pageId);
        if (oldPost == null || oldPost.getType() == 2) {
            throw new PostNotExistException();
        }
        if (oldPost.getType() == 1) {
            oldPosts = new ArrayList<>();
            oldPosts.add(oldPost);
        } else {
            oldPosts = listMultiPosts(weixinAppid, oldPost.getMediaId(), true);
        }

        // 数目不对应, 接口调用错误
        if (oldPosts.size() != posts.size()) {
            throw new RuntimeException("不能修改图文的数目:" + oldPosts.size());
        }

        String accessToken = accountService.getAccessToken(weixinAppid);

        // 对图文数据做预处理
        posts = cleanPosts(posts, accessToken);
        // 更新到微信的图文数据
        List<PostDO> wxPosts = wrapWeiXinPosts(accessToken, posts);

        // 修改微信后台文章
        String mediaId = oldPost.getMediaId();

        int curIndex = 0;
        int curUpdateTime = 0;
        try {
            for (int index = 0; index < wxPosts.size(); index++) {
                if (! wxPostEqual(oldPosts.get(index), posts.get(index))) {
                    weixinPostService.updatePost(accessToken, oldPost.getMediaId(), wxPosts.get(index));
                    curIndex = index;
                    curUpdateTime++;
                }
            }
        } catch (MediaIdNotExistException e) {
            // 第一篇图文报mediaId不存在，可能是mediaId，也可能是thumbMediaId。
            if (curUpdateTime == 0) {
                // 尝试新建
                mediaId = weixinPostService.uploadPosts(accessToken, wxPosts);
            } else {
                throw new ThumbMediaIdNotExistException("第" + (curIndex + 1) + "篇图文的封面图在微信后台被删除，请重新上传");
            }
        }

        // 数据修正
        for (PostDO postDO : posts) {
            postDO.setMediaId(mediaId);
            postDO.setWeixinAppid(weixinAppid);
        }
        // pageId修正
        for (int index = 0; index < posts.size(); index++) {
            posts.get(index).setPageId(oldPosts.get(index).getPageId());
        }


        updateMultiPostsDB(weixinAppid, oldPost.getMediaId(), posts);
    }

    /**
     * 比较两个post上传给微信的字段是否不同
     */
    private boolean wxPostEqual(PostDO postDO1, PostDO postDO2) {
        if (!Objects.equals(postDO1.getTitle(), postDO2.getTitle())) {
            return false;
        }
        if (!Objects.equals(postDO1.getThumbMediaId(), postDO2.getThumbMediaId())) {
            return false;
        }
        if (!Objects.equals(postDO1.getAuthor(), postDO2.getAuthor())) {
            return false;
        }
        if (!Objects.equals(postDO1.getDigest(), postDO2.getDigest())) {
            return false;
        }
        if (!Objects.equals(postDO1.getShowCoverPic(), postDO2.getShowCoverPic())) {
            return false;
        }
        if (!Objects.equals(postDO1.getContentSourceUrl(), postDO2.getContentSourceUrl())) {
            return false;
        }
        if (!Objects.equals(postDO1.getContent(), postDO2.getContent())) {
            return false;
        }
        return true;
    }


    /**
     * 修改图文的数据存储
     * 此方法假设图文的数目不变
     */
    private void updateMultiPostsDB(long weixinAppid, String mediaId, List<PostDO> posts) {
        // 更新每篇图文的数据库，修改内容
        StringBuilder titleSum = new StringBuilder();
        for (PostDO postDO: posts) {
            postDao.updatePost(postDO, postDO.getPageId());
            mongoPostDao.upsertPost(postDO.getPageId(), postDO.getContent());
            titleSum.append(postDO.getTitle());
        }

        // 修改图文总记录
        if (posts.size() > 1) {
            PostDO postSum = postDao.getPostSum(weixinAppid, mediaId);

            postSum.setTitle(titleSum.toString());
            postSum.setMediaId(posts.get(0).getMediaId());
            postDao.updatePost(postSum, postSum.getPageId());
        }
    }

    /**
     * 把存数据库的多图文DO对象，封装成同步给微信的。
     * 替换了内容中的图片url为wx_src
     */
    private List<PostDO> wrapWeiXinPosts(String accessToken, List<PostDO> posts) throws Exception {
        List<PostDO> wxPosts = new ArrayList<>();

        for (PostDO postDO : posts) {
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
            // 设置顺序
            wxPost.setIndex(postDO.getIndex());

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

        int index =0;
        short type = (short) (posts.size() > 1 ? 3 : 1);

        for (PostDO postDO : posts) {
            // 设置index顺序
            postDO.setIndex(index++);
            // 图文类型
            postDO.setType(type);
            // 上传图片
            String replacedContent = uploadContentImg(accessToken, postDO.getContent());
            // 过滤content中的js事件
            replacedContent = filterHtml(replacedContent);
            postDO.setContent(replacedContent);
            // 删除emoji
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
            postDO.setThumbMediaId(thumbMediaId);

            // 清除封面尺寸---"/imageView/v1/thumbnail"
            postDO.setThumbUrl(postDO.getThumbUrl().replaceAll("/imageView/v1/thumbnail.*$", ""));
            // 默认设置showCoverPic为0
            if (postDO.getShowCoverPic() == null){
                postDO.setShowCoverPic((short) 0);
            }
        }

        return posts;
    }


    /**
     * 把校验后的多图文新增到数据库, 必须先经过cleanPosts清洗数据
     */
    private void saveMultiPosts(long weixinAppid, List<PostDO> posts) {

        // 1. 单图文 2. 多图文总记录 3. 多图文中的一条
        short type = (short) (posts.size() > 1 ? 3 : 1);

        int index = 0;
        for (PostDO postDO : posts) {

            // 生成pageId
            postDO.setPageId(IdGeneratorUtil.getID());
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
            // 有效数据
            sumPost.setWeixinAppid(weixinAppid);
            sumPost.setPageId(IdGeneratorUtil.getID());
            sumPost.setType((short) 2);
            sumPost.setIndex(0);
            sumPost.setMediaId(posts.get(0).getMediaId());
            sumPost.setUpdateTime(posts.get(0).getUpdateTime());
            // 把title拼接起来，保存多图文总记录里面
            StringBuilder sumTitle = new StringBuilder();
            for (PostDO postDO : posts) {
                sumTitle.append(postDO.getTitle());
            }
            sumPost.setTitle(sumTitle.toString());

            // 无效数据
            sumPost.setThumbMediaId("");
            sumPost.setThumbUrl("");
            sumPost.setAuthor("");
            sumPost.setDigest("");
            sumPost.setContentSourceUrl("");
            sumPost.setShowCoverPic((short) 0);

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
    public void syncWeixinPosts(long weixinAppid, long uid) throws SyncWXPostTooOftenException {
        if (! redisPostDao.couldSyncWxPost(weixinAppid)) {
            throw new SyncWXPostTooOftenException();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("weixinAppid", weixinAppid);
        map.put("uid", uid);
        mqUtil.publish(MqConstant.IMPORT_WEIXIN_POST_LIST, map);
    }

    @Override
    public void importWeixinPost(PostDTO.PostItem postItem, long userId) throws Exception {
        // 将微信返回的文章处理为数据库存储的图文
        List<PostDO> postDOList = new LinkedList<>();
        List<PostDTO.Item.Content.NewsItem> newsItems = postItem.getItem().getContent().getNewsItems();
        int updateTime = postItem.getItem().getUpdateTime();
        logger.info("----> updateTime" + updateTime);
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
            postDO.setUpdateTime(updateTime);
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
        List<PostDTO> postDTOList = weixinPostService.listAllPosts(accountService.getAccessToken(weixinAppid));

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
                logger.debug("----> groupCount:" + matcher.groupCount());
                String result = "url(" + getKzImgUrlByWeixinImgUrl(matcher.group(1), userId) + ")";
                logger.debug("----> origin: " + matcher.group(0) + " result:" + result);
                return result;
            }
        };
        regex = "url\\(\"?'?(?:&quot;)?(https?:\\/\\/mmbiz[^)]+?)(?:&quot;)?\"?'?\\)";
        replaceCallbackMatcher = new ReplaceCallbackMatcher(regex);
        content = replaceCallbackMatcher.replaceMatches(content, callback);

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
        return KzApiConfig.getResUrl("/res/weixin/images/post-default-cover-900-500.png");
    }

    /**
     * 多图文非第一篇图文的默认封面图
     *
     * @return
     */
    private String getCommonPostDefaultThumbUrl() {
        return KzApiConfig.getResUrl("/res/weixin/images/post-default-cover-200-200.png");
    }

}
