package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.config.KzApiConfig;
import com.kuaizhan.constant.ErrorCodes;
import com.kuaizhan.constant.MqConstant;
import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.dao.mapper.PostDao;
import com.kuaizhan.dao.redis.RedisPostDao;
import com.kuaizhan.exception.BusinessException;
import com.kuaizhan.exception.deprecated.business.*;
import com.kuaizhan.dao.mongo.MongoPostDao;
import com.kuaizhan.exception.common.KZPicUploadException;
import com.kuaizhan.exception.common.MediaIdNotExistException;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.ArticleDTO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.pojo.DTO.WxPostListDTO;
import com.kuaizhan.pojo.DTO.WxPostDTO;
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
import java.util.*;
import java.util.regex.Matcher;

/**
 * Created by zixiong on 2017/3/20.
 */
@Service("postService")
public class PostServiceImpl implements PostService {

    private static final Logger logger = Logger.getLogger(PostServiceImpl.class);

    // 最长的title字符数
    private static final int TITLE_MAX = 640;

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
    public Page<PostDO> listPostsByPagination(long weixinAppid, String title, Integer page, Boolean flat) {

        Page<PostDO> postDOPage = new Page<>(page, AppConstant.PAGE_SIZE_MIDDLE);

        List<PostDO> posts;
        posts = postDao.listPostsByPagination(weixinAppid, title, postDOPage, flat);

        long totalCount = postDao.count(weixinAppid, title, flat);
        postDOPage.setTotalCount(totalCount);
        postDOPage.setResult(posts);

        return postDOPage;
    }

    @Override
    public List<PostDO> listMultiPosts(long weixinAppid, String mediaId, Boolean withContent) {

        List<PostDO> multiPosts = postDao.listMultiPosts(weixinAppid, mediaId);

        // 从Mongo中取content
        if (withContent) {
            for (PostDO postDO : multiPosts) {
                postDO.setContent(getPostContent(postDO.getPageId()));
            }
        }

        return multiPosts;
    }

    @Override
    public void deletePost(long weixinAppid, long pageId, String accessToken) {

        PostDO postDO = getPostByPageId(pageId);
        if (postDO != null) {
            String mediaId = postDO.getMediaId();
            //微信删除
            weixinPostService.deletePost(mediaId, accessToken);
            //数据库删除
            postDao.deletePost(weixinAppid, mediaId);
        }
    }

    @Override
    public Boolean exist(long weixinAppid, String mediaId) {
        return postDao.exist(weixinAppid, mediaId);
    }

    @Override
    public PostDO getPostByPageId(long pageId) {

        PostDO postDO = postDao.getPost(pageId);
        if (postDO != null) {
            postDO.setContent(getPostContent(pageId));
        }
        return postDO;
    }

    public PostDO getPostByMediaId(long weixinAppid, String mediaId) {
        return postDao.getPostByMediaId(weixinAppid, mediaId);
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
    public String getPostWxUrl(long weixinAppid, long pageId) throws AccountNotExistException {
        PostDO postDO = getPostByPageId(pageId);
        if (postDO != null) {
            String wxUrl = postDO.getPostUrl();
            if (wxUrl != null && wxUrl.contains("mp.weixin.qq.com")) {
                return wxUrl;
            // 更新
            } else {
                List<WxPostDTO> wxPostDTOS = weixinPostService.getWxPost(postDO.getMediaId(), accountService.getAccessToken(weixinAppid));
                // 简化url
                List<String> urls = new ArrayList<>();
                for (WxPostDTO postDTO: wxPostDTOS) {
                    urls.add(postDTO.getUrl().replaceAll("&chksm=[^&]+", ""));
                }
                // 单图文
                if (postDO.getType() == 1) {
                    if (urls.size() != 1) {
                        throw new BusinessException(ErrorCodes.DIFFERENT_POSTS_NUM_ERROR);
                    }
                    PostDO updatePostDo = new PostDO();
                    updatePostDo.setPostUrl(urls.get(0));
                    postDao.updatePost(updatePostDo, pageId);
                    return urls.get(0);
                // 多图文
                } else {
                    List<PostDO> multiPosts = listMultiPosts(weixinAppid, postDO.getMediaId(), false);
                    if (urls.size() != multiPosts.size()) {
                        throw new BusinessException(ErrorCodes.DIFFERENT_POSTS_NUM_ERROR);
                    }
                    for (int i = 0; i < urls.size(); i++ ) {
                        PostDO updatePostDo = new PostDO();
                        updatePostDo.setPostUrl(urls.get(i));
                        postDao.updatePost(updatePostDo, multiPosts.get(i).getPageId());
                    }
                    return urls.get(postDO.getIndex());
                }
            }
        }
        return null;
    }

    @Override
    public void export2KzArticle(long pageId, long categoryId, long siteId) {
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
                throw new BusinessException(ErrorCodes.OPERATION_FAILED);
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
            throw new BusinessException(ErrorCodes.POST_NOT_EXIST_ERROR);
        }
        if (oldPost.getType() == 1) {
            oldPosts = new ArrayList<>();
            oldPosts.add(oldPost);
        } else {
            oldPosts = listMultiPosts(weixinAppid, oldPost.getMediaId(), true);
        }

        // 数目不对应, 接口调用错误
        if (oldPosts.size() != posts.size()) {
            throw new ParamException("由于微信限制，不能修改多图文的数目:" + oldPosts.size());
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
                // 清空post url
                for (PostDO postDO: posts) {
                    postDO.setPostUrl("");
                }
            } else {
                String message = "第" + (curIndex + 1) + "篇图文的封面图在微信后台被删除，请重新上传";
                throw new BusinessException(ErrorCodes.THUMB_MEDIA_ID_NOT_EXIST_ERROR, message);
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

        int updateTime = (int) (System.currentTimeMillis() / 1000);

        for (PostDO postDO: posts) {

            postDO.setUpdateTime(updateTime);
            postDao.updatePost(postDO, postDO.getPageId());
            mongoPostDao.upsertPost(postDO.getPageId(), postDO.getContent());
            titleSum.append(postDO.getTitle());
        }

        // 修改图文总记录
        if (posts.size() > 1) {
            PostDO postSum = postDao.getPostByMediaId(weixinAppid, mediaId);
            if (postSum.getType() != 2) {
                logger.error("【图文垃圾数据】, weixinAppid:" + weixinAppid + " mediaId:" + mediaId);
            }

            postSum.setUpdateTime(updateTime);
            int endIndex = titleSum.length() < TITLE_MAX? titleSum.length(): TITLE_MAX;
            postSum.setTitle(titleSum.toString().substring(0, endIndex));
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


    /*** 清理微信图文数据 ***/
    private List<PostDO> cleanWxPosts(long weixinAppid, String mediaId, long updateTime, long userId, List<WxPostDTO> wxPostDTOs) throws Exception {
        int key = 0;
        List<PostDO> postDOList = new ArrayList<>();
        for (WxPostDTO wxPostDTO : wxPostDTOs) {

            PostDO postDO = new PostDO();
            postDO.setTitle(wxPostDTO.getTitle());
            postDO.setDigest(wxPostDTO.getDigest());
            postDO.setAuthor(wxPostDTO.getAuthor());
            postDO.setThumbMediaId(wxPostDTO.getThumbMediaId());
            postDO.setShowCoverPic(wxPostDTO.getShowCoverPic());
            postDO.setPostUrl(wxPostDTO.getUrl().replaceAll("&chksm=[^&]+", ""));
            postDO.setContentSourceUrl(wxPostDTO.getContentSourceUrl() == null ? "" : wxPostDTO.getContentSourceUrl());
            // 内容替换图片及视频
            String content = replacePicUrlFromWeixinPost(wxPostDTO.getContent(), userId);
            postDO.setContent(replaceVideoFromWeixinPost(content));
            // 缩略图链接替换
            String picUrl;
            if (wxPostDTO.getThumbUrl() == null || "".equals(wxPostDTO.getThumbUrl())) {
                if (key == 0) {
                    picUrl = KzApiConfig.getResUrl("/res/weixin/images/post-default-cover-900-500.png");
                } else {
                    picUrl = KzApiConfig.getResUrl("/res/weixin/images/post-default-cover-200-200.png");
                }
            } else {
                picUrl = getKzImgUrlByWeixinImgUrl(wxPostDTO.getThumbUrl(), userId);
            }
            postDO.setThumbUrl(picUrl);

            postDO.setWeixinAppid(weixinAppid);
            postDO.setMediaId(mediaId);
            postDO.setUpdateTime((int) updateTime);

            postDOList.add(postDO);
            key++;
        }
        // 继续清理数据
        return cleanPosts(postDOList, accountService.getAccessToken(weixinAppid));
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
            int endIndex = sumTitle.length() < TITLE_MAX? sumTitle.length(): TITLE_MAX;
            sumPost.setTitle(sumTitle.toString().substring(0, endIndex));

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
        String regex = "(<\\S+?\\s+?)(on[a-z]+?=[\"'][^\"']*?[\"])([^>]*?>)";
        ReplaceCallbackMatcher callbackMatcher = new ReplaceCallbackMatcher(regex);
        html = callbackMatcher.replaceMatches(html, matcher -> matcher.group(1) + matcher.group(3));

        return html;
    }

    /**
     * 上传图文content中的图片到微信，并写入wx_src到img标签
     */
    private String uploadContentImg(final String accessToken, String content) throws Exception {
        // 对wx_src垃圾数据进行清理，即wx_src标签下的内容不是微信链接。
        String wxSrcRegex = "(wx_src=)[\"'](?<wxSrc>[^\"']+?)[\"']";
        ReplaceCallbackMatcher callbackMatcher = new ReplaceCallbackMatcher(wxSrcRegex);

        content = callbackMatcher.replaceMatches(content,
                matcher -> {
                    String wxSrc = matcher.group("wxSrc");
                    wxSrc = weixinPostService.uploadImgForPost(accessToken, wxSrc);
                    return "wx_src=\"" + wxSrc + "\"";
                }
        );

        // 正常为图片标签建立wxSrc
        String regex = "(<img[^>]* src=)[\"'](?<src>[^\"']+)[\"']([^>]*>)";
        callbackMatcher = new ReplaceCallbackMatcher(regex);
        content = callbackMatcher.replaceMatches(content,
                matcher -> {
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
        );

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

        // 替换的回调
        ReplaceCallbackMatcher.Callback callback =
                matcher -> matcher.group(1) +
                        matcher.group("wxSrc") +
                        matcher.group(3) +
                        matcher.group("wxSrc") +
                        matcher.group(5);

        // wx_src在src前
        String regex = "(<img[^>]* src=\")(?<src>[^\"]+)(\"[^>]* wx_src=\")(?<wxSrc>[^\"]+)(\"[^>]*>)";
        ReplaceCallbackMatcher callbackMatcher = new ReplaceCallbackMatcher(regex);
        content = callbackMatcher.replaceMatches(content, callback);

        // wx_src在src后
        regex = "(<img[^>]* wx_src=\")(?<wxSrc>[^\"]+)(\"[^>]* src=\")(?<src>[^\"]+)(\"[^>]*>)";
        callbackMatcher = new ReplaceCallbackMatcher(regex);
        content = callbackMatcher.replaceMatches(content, callback);

        // 替换background-image
        regex = "(background-image: url\\()([^\\)]+)(\\))";
        callbackMatcher = new ReplaceCallbackMatcher(regex);
        content = callbackMatcher.replaceMatches(content,
                matcher -> matcher.group(1) +
                        weixinPostService.uploadImgForPost(accessToken, matcher.group(2)) + matcher.group(3)
        );

        // TODO: 对中转链接的替换； 校验上传成功的微信图片，肯定是小于1M的。
        return content;
    }

    @Override
    public void syncWeixinPosts(long weixinAppid, long userId) {
        if (! redisPostDao.couldSyncWxPost(weixinAppid)) {
            throw new BusinessException(ErrorCodes.SYNC_WX_POST_TOO_OFTEN_ERROR);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("weixinAppid", weixinAppid);
        map.put("uid", userId);
        mqUtil.publish(MqConstant.IMPORT_WEIXIN_POST_LIST, map);
    }

    @Override
    public void calSyncWeixinPosts(long weixinAppid, long userId) throws AccountNotExistException {
        int fetchCount = 20;
        String accessToken = accountService.getAccessToken(weixinAppid);

        WxPostListDTO postListDTO = weixinPostService.getWxPostList(accessToken, 0, fetchCount);
        int total = postListDTO.getTotalCount();
        int index = postListDTO.getItemCount();

        for (WxPostListDTO.Item item: postListDTO.getItems()) {
            List<WxPostDTO> posts = item.getContent().getNewsItems();
            publishSyncWeixinPost(posts, item.getMediaId(), item.getUpdateTime(), weixinAppid, userId);
        }

        while (index < total) {
            postListDTO = weixinPostService.getWxPostList(accessToken, index, fetchCount);
            index += postListDTO.getItemCount();
            for (WxPostListDTO.Item item: postListDTO.getItems()) {
                List<WxPostDTO> posts = item.getContent().getNewsItems();
                publishSyncWeixinPost(posts, item.getMediaId(), item.getUpdateTime(), weixinAppid, userId);
            }
        }

    }

    /*** 判断是否应该同步微信图文，并发布mq消息 ***/
    private void publishSyncWeixinPost(List<WxPostDTO> wxPostDTOs, String mediaId, int updateTime, long weixinAppid, long userId) {
        PostDO postDO = getPostByMediaId(weixinAppid, mediaId);
        // 需要新增或者需要更新
        if (postDO == null || updateTime - postDO.getUpdateTime() > 2) {
            Map<String, Object> message = new HashMap<>();
            message.put("userId", userId);
            message.put("weixinAppid", weixinAppid);
            message.put("mediaId", mediaId);
            message.put("updateTime", updateTime);
            message.put("wxPostDTOs", JsonUtil.bean2String(wxPostDTOs));
            message.put("isNew", postDO == null);
            mqUtil.publish(MqConstant.IMPORT_WEIXIN_POST, message);
        }
    }


    @Override
    public void importWeixinPost(long weixinAppid, String mediaId, long updateTime, long userId, List<WxPostDTO> wxPostDTOs) throws Exception {
        List<PostDO> postDOList = cleanWxPosts(weixinAppid, mediaId, updateTime, userId, wxPostDTOs);
        // 入库
        saveMultiPosts(weixinAppid, postDOList);
    }

    @Override
    public void updateWeixinPost(long weixinAppid, String mediaId, long updateTime, long userId, List<WxPostDTO> wxPostDTOs) throws Exception {
        List<PostDO> postDOList = cleanWxPosts(weixinAppid, mediaId, updateTime, userId, wxPostDTOs);

        // 修改数据库
        PostDO postDO = getPostByMediaId(weixinAppid, mediaId);
        // 多图文情况
        List<PostDO> multiPosts = new ArrayList<>();
        if (postDO.getType() == 2) {
            multiPosts = listMultiPosts(weixinAppid, mediaId, false);
        }

        // 再次校验修改时间，以免覆盖在平台的修改
        if (updateTime - postDO.getUpdateTime() > 2) {
            // 现在是单图文
            if (postDOList.size() == 1) {
                PostDO newPost = postDOList.get(0);
                newPost.setUpdateTime((int) updateTime);
                newPost.setPageId(postDO.getPageId());
                // 更新内容
                mongoPostDao.upsertPost(newPost.getPageId(), newPost.getContent());
                postDao.updatePost(newPost, newPost.getPageId());

                // 清除其他图文
                for (PostDO multiPost: multiPosts) {
                    postDao.deletePostByPageId(multiPost.getPageId());
                }
            }
            // 现在是多图文
            else {
                StringBuilder sumTitle = new StringBuilder();
                for (int i = 0; i < postDOList.size(); i++) {
                    PostDO newPost = postDOList.get(i);
                    newPost.setUpdateTime((int) updateTime);
                    sumTitle.append(newPost.getTitle());

                    // 存在老图文
                    if (i < multiPosts.size()) {
                        newPost.setPageId(multiPosts.get(i).getPageId());
                        mongoPostDao.upsertPost(newPost.getPageId(), newPost.getContent());
                        postDao.updatePost(newPost, newPost.getPageId());
                    }
                    // 不存在老图文则新建
                    else {
                        newPost.setPageId(IdGeneratorUtil.getID());
                        mongoPostDao.upsertPost(newPost.getPageId(), newPost.getContent());
                        postDao.insertPost(newPost);
                    }
                }
                // 删除多余图文
                for (int i = postDOList.size(); i < multiPosts.size(); i++) {
                    postDao.deletePostByPageId(multiPosts.get(i).getPageId());
                }
                // 修改图文总记录
                postDO.setWeixinAppid(weixinAppid);
                postDO.setType((short) 2);
                postDO.setIndex(0);
                postDO.setMediaId(mediaId);
                postDO.setUpdateTime((int) updateTime);
                postDO.setTitle(sumTitle.toString());
                postDO.setThumbMediaId("");
                postDO.setThumbUrl("");
                postDO.setAuthor("");
                postDO.setDigest("");
                postDO.setPostUrl("");
                postDO.setContentSourceUrl("");
                postDO.setShowCoverPic((short) 0);
                postDao.updatePost(postDO, postDO.getPageId());
            }
        }
    }


    /*** 微信图片地址生成快站图片url，若失败则返回原url ***/
     private String getKzImgUrlByWeixinImgUrl(String imgUrl, long userId) {
        // 若不是微信图片则返回原url
        if (!imgUrl.contains("mmbiz")) return imgUrl;

        // 去掉微信图片中的tp=webp
         String kzPicUrl = imgUrl;
         kzPicUrl = kzPicUrl.replaceAll("&tp=webp", "");
         kzPicUrl = kzPicUrl.replaceAll("\\?tp=webp", "");
         // 去掉所有&amp;后的内容
         kzPicUrl = kzPicUrl.replaceAll("&amp;.*$", "");

        try {
            return kZPicService.uploadByUrlAndUserId(kzPicUrl, userId);
        } catch (KZPicUploadException e) {
            LogUtil.logMsg(e);
        }

        // 上传失败，返回原始url
        return imgUrl;
    }

    /*** 替换微信内容中的微信图片链接为快站链接 ***/
    private String replacePicUrlFromWeixinPost(String content, final long userId) throws Exception {
        // 将文章中的图片的data-src替换为src
        content = content.replaceAll("(<img [^>]*)(data-src)", "$1src");

        // img中的微信图片转成快站链接,记录wx_src
        String regex = "(<img[^>]* src=\")([^\"]+)(\"[^>]*>)";
        ReplaceCallbackMatcher replaceCallbackMatcher = new ReplaceCallbackMatcher(regex);
        content = replaceCallbackMatcher.replaceMatches(content,
                matcher -> matcher.group(1)
                        + getKzImgUrlByWeixinImgUrl(matcher.group(2), userId)
                        + "\" wx_src=\""
                        + matcher.group(2)
                        + matcher.group(3)
        );

        regex = "(<img[^>]* src=')([^']+)('[^>]*>)";
        replaceCallbackMatcher = new ReplaceCallbackMatcher(regex);
        content = replaceCallbackMatcher.replaceMatches(content,
                matcher -> matcher.group(1)
                        + getKzImgUrlByWeixinImgUrl(matcher.group(2), userId)
                        + "' wx_src='"
                        + matcher.group(2)
                        + matcher.group(3)
        );

        // 背景图中的微信图片转成快站链接
        regex = "url\\(\"?'?(?:&quot;)?(https?:\\/\\/mmbiz[^)]+?)(?:&quot;)?\"?'?\\)";
        replaceCallbackMatcher = new ReplaceCallbackMatcher(regex);
        content = replaceCallbackMatcher.replaceMatches(content,
                matcher -> "url(" + getKzImgUrlByWeixinImgUrl(matcher.group(1), userId) + ")"
        );

        return content;
    }

    /*** 替换微信图文中视频链接 ***/
    private String replaceVideoFromWeixinPost(String content) {
        return content.replaceAll(
                "<iframe[^>]*class=\"video_iframe\"[^>]*data-src=\"([^\"]+)vid=([^&]+)([^\"]+)\"[^>]*>",
                "<iframe style=\"z-index:1;\" class=\"video_iframe\" data-src=\"$1vid=$2$3\" " +
                        "frameborder=\"0\" allowfullscreen=\"\" " +
                        "src=\"https://v.qq.com/iframe/player.html?vid=$2&tiny=0&auto=0\" width=\"280px\" height=\"100%\">"
        );
    }
}
