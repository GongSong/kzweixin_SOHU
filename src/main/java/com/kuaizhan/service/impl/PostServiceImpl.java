package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.config.KzApiConfig;
import com.kuaizhan.constant.ErrorCode;
import com.kuaizhan.constant.MqConstant;
import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.dao.mapper.PostDao;
import com.kuaizhan.dao.redis.RedisImageDao;
import com.kuaizhan.dao.redis.RedisPostDao;
import com.kuaizhan.exception.BusinessException;
import com.kuaizhan.exception.common.DownloadFileFailedException;
import com.kuaizhan.exception.kuaizhan.Export2KzException;
import com.kuaizhan.exception.kuaizhan.GetKzArticleException;
import com.kuaizhan.exception.deprecated.business.*;
import com.kuaizhan.dao.mongo.MongoPostDao;
import com.kuaizhan.exception.kuaizhan.KZPicUploadException;
import com.kuaizhan.exception.weixin.WxMediaIdNotExistException;
import com.kuaizhan.manager.KzManager;
import com.kuaizhan.manager.WxPostManager;
import com.kuaizhan.pojo.po.PostPO;
import com.kuaizhan.pojo.dto.ArticleDTO;
import com.kuaizhan.pojo.dto.Page;
import com.kuaizhan.pojo.dto.WxPostListDTO;
import com.kuaizhan.pojo.dto.WxPostDTO;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.PostService;
import com.kuaizhan.utils.*;
import com.vdurmont.emoji.EmojiParser;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by zixiong on 2017/3/20.
 */
@Service("postService")
public class PostServiceImpl implements PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    // 最长的title字符数
    private static final int TITLE_MAX = 640;

    @Resource
    private PostDao postDao;
    @Resource
    private RedisImageDao redisImageDao;
    @Resource
    private RedisPostDao redisPostDao;
    @Resource
    private MqUtil mqUtil;
    @Resource
    private MongoPostDao mongoPostDao;
    @Resource
    private AccountService accountService;

    @Override
    public long genPageId() {
        final long MIN = 1000000000L;
        final long MAX = 9999999999L;
        int count = 1;

        long pageId = ThreadLocalRandom.current().nextLong(MIN, MAX + 1);
        while (postDao.isPageIdExist(pageId)) {
            pageId = ThreadLocalRandom.current().nextLong(MIN, MAX + 1);
            count++;
        }
        // 随机三次及以上才随机到，报警
        if (count >= 3) {
            logger.warn("[genPageId] gen times reach {}", count);
        }
        return pageId;
    }


    @Override
    public Page<PostPO> listPostsByPagination(long weixinAppid, String title, Integer pageNum, Boolean flat) {

        Page<PostPO> page = new Page<>(pageNum, AppConstant.PAGE_SIZE_MIDDLE);

        List<PostPO> posts;
        posts = postDao.listPostsByPagination(weixinAppid, title, page, flat);

        long totalCount = postDao.count(weixinAppid, title, flat);
        page.setTotalCount(totalCount);
        page.setResult(posts);

        return page;
    }

    @Override
    public List<PostPO> listMultiPosts(long weixinAppid, String mediaId, Boolean withContent) {

        List<PostPO> multiPosts = postDao.listMultiPosts(weixinAppid, mediaId);

        // 从Mongo中取content
        if (withContent) {
            for (PostPO postPO : multiPosts) {
                postPO.setContent(getPostContent(postPO.getPageId()));
            }
        }

        return multiPosts;
    }

    @Override
    public void deletePost(long weixinAppid, long pageId, String accessToken) {

        PostPO postPO = getPostByPageId(pageId);
        if (postPO != null) {
            String mediaId = postPO.getMediaId();
            //微信删除
            WxPostManager.deletePost(mediaId, accessToken);
            //数据库删除
            postDao.deletePost(weixinAppid, mediaId);
        }
    }

    @Override
    public Boolean exist(long weixinAppid, String mediaId) {
        return postDao.exist(weixinAppid, mediaId);
    }

    @Override
    public PostPO getPostByPageId(long pageId) {

        PostPO postPO = postDao.getPost(pageId);
        if (postPO != null) {
            postPO.setContent(getPostContent(pageId));
        }
        return postPO;
    }

    public PostPO getPostByMediaId(long weixinAppid, String mediaId) {
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
                    logger.info("从快站接口获取内容成功, pageId:{}", pageId);
                    mongoPostDao.upsertPost(pageId, content);
                }
            }
        }
        return content;
    }

    @Override
    public ArticleDTO getKzArticle(long pageId) throws GetKzArticleException {
        // TODO: 这一块，联合mq发布消息一起重构
        return KzManager.getKzArticle(pageId);
    }

    @Override
    public void importKzArticle(long weixinAppid, List<Long> pageIds) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("weixinAppid", weixinAppid);
        param.put("pageIds", pageIds);

        mqUtil.publish(MqConstant.IMPORT_KUAIZHAN_POST, param);
    }

    @Override
    public String getPostWxUrl(long weixinAppid, long pageId) {
        PostPO postPO = getPostByPageId(pageId);
        if (postPO != null) {
            String wxUrl = postPO.getPostUrl();
            if (wxUrl != null && wxUrl.contains("mp.weixin.qq.com")) {
                return wxUrl;
            // 更新
            } else {
                List<WxPostDTO> wxPostDTOS = WxPostManager.getWxPost(postPO.getMediaId(), accountService.getAccessToken(weixinAppid));
                // 简化url
                List<String> urls = new ArrayList<>();
                for (WxPostDTO postDTO: wxPostDTOS) {
                    urls.add(postDTO.getUrl().replaceAll("&chksm=[^&]+", ""));
                }
                // 单图文
                if (postPO.getType() == 1) {
                    if (urls.size() != 1) {
                        throw new BusinessException(ErrorCode.DIFFERENT_POSTS_NUM_ERROR);
                    }
                    PostPO updatePostPO = new PostPO();
                    updatePostPO.setPostUrl(urls.get(0));
                    postDao.updatePost(updatePostPO, pageId);
                    return urls.get(0);
                // 多图文
                } else {
                    List<PostPO> multiPosts = listMultiPosts(weixinAppid, postPO.getMediaId(), false);
                    if (urls.size() != multiPosts.size()) {
                        throw new BusinessException(ErrorCode.DIFFERENT_POSTS_NUM_ERROR);
                    }
                    for (int i = 0; i < urls.size(); i++ ) {
                        PostPO updatePostPO = new PostPO();
                        updatePostPO.setPostUrl(urls.get(i));
                        postDao.updatePost(updatePostPO, multiPosts.get(i).getPageId());
                    }
                    return urls.get(postPO.getIndex());
                }
            }
        }
        return null;
    }

    @Override
    public void export2KzArticle(long pageId, long categoryId, long siteId) {
        PostPO postPO = getPostByPageId(pageId);
        if (postPO == null) {
            logger.warn("[export2KzArticle] post is null, pageId: {} siteId: {}", pageId, siteId);
            return;
        }

        String content = getPostContent(pageId);
        try {
            KzManager.export2KzArticle(siteId, categoryId, postPO, content);
        } catch (Export2KzException e) {
            logger.error("[export2KzArticle] export failed", e);
            throw new BusinessException(ErrorCode.OPERATION_FAILED, "导出图文失败，请稍后重试");
        }
    }

    @Override
    public void insertMultiPosts(long weixinAppid, List<PostPO> posts) throws Exception {

        // 在方法执行过程中，有失效的风险
        String accessToken = accountService.getAccessToken(weixinAppid);

        // 对图文数据做预处理
        posts = cleanPosts(posts, accessToken);

        // 上传到微信
        String mediaId = WxPostManager.uploadPosts(accessToken, wrapWeiXinPosts(accessToken, posts));
        for (PostPO postPO : posts) {
            postPO.setMediaId(mediaId);
        }

        // 入库
        saveMultiPosts(weixinAppid, posts);
    }

    @Override
    public void updateMultiPosts(long weixinAppid, long pageId, List<PostPO> posts) throws Exception {

        // 老图文
        List<PostPO> oldPosts;
        PostPO oldPost = getPostByPageId(pageId);
        if (oldPost == null || oldPost.getType() == 2) {
            throw new BusinessException(ErrorCode.POST_NOT_EXIST_ERROR);
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
        List<PostPO> wxPosts = wrapWeiXinPosts(accessToken, posts);

        // 修改微信后台文章
        String mediaId = oldPost.getMediaId();

        int curIndex = 0;
        int curUpdateTime = 0;
        try {
            for (int index = 0; index < wxPosts.size(); index++) {
                if (! wxPostEqual(oldPosts.get(index), posts.get(index))) {
                    WxPostManager.updatePost(accessToken, oldPost.getMediaId(), wxPosts.get(index));
                    curIndex = index;
                    curUpdateTime++;
                }
            }
        } catch (WxMediaIdNotExistException e) {
            // 第一篇图文报mediaId不存在，可能是mediaId，也可能是thumbMediaId。
            if (curUpdateTime == 0) {
                // 尝试新建
                mediaId = WxPostManager.uploadPosts(accessToken, wxPosts);
                // 清空post url
                for (PostPO postPO : posts) {
                    postPO.setPostUrl("");
                }
            } else {
                String message = "第" + (curIndex + 1) + "篇图文的封面图在微信后台被删除，请重新上传";
                throw new BusinessException(ErrorCode.THUMB_MEDIA_ID_NOT_EXIST_ERROR, message);
            }
        }

        // 数据修正
        for (PostPO postPO : posts) {
            postPO.setMediaId(mediaId);
            postPO.setWeixinAppid(weixinAppid);
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
    private boolean wxPostEqual(PostPO postPO1, PostPO postPO2) {
        if (!Objects.equals(postPO1.getTitle(), postPO2.getTitle())) {
            return false;
        }
        if (!Objects.equals(postPO1.getThumbMediaId(), postPO2.getThumbMediaId())) {
            return false;
        }
        if (!Objects.equals(postPO1.getAuthor(), postPO2.getAuthor())) {
            return false;
        }
        if (!Objects.equals(postPO1.getDigest(), postPO2.getDigest())) {
            return false;
        }
        if (!Objects.equals(postPO1.getShowCoverPic(), postPO2.getShowCoverPic())) {
            return false;
        }
        if (!Objects.equals(postPO1.getContentSourceUrl(), postPO2.getContentSourceUrl())) {
            return false;
        }
        if (!Objects.equals(postPO1.getContent(), postPO2.getContent())) {
            return false;
        }
        return true;
    }


    /**
     * 修改图文的数据存储
     * 此方法假设图文的数目不变
     */
    private void updateMultiPostsDB(long weixinAppid, String mediaId, List<PostPO> posts) {
        // 更新每篇图文的数据库，修改内容
        StringBuilder titleSum = new StringBuilder();

        int updateTime = (int) (System.currentTimeMillis() / 1000);

        for (PostPO postPO : posts) {

            postPO.setUpdateTime(updateTime);
            postDao.updatePost(postPO, postPO.getPageId());
            mongoPostDao.upsertPost(postPO.getPageId(), postPO.getContent());
            titleSum.append(postPO.getTitle());
        }

        // 修改图文总记录
        if (posts.size() > 1) {
            PostPO postSum = postDao.getPostByMediaId(weixinAppid, mediaId);
            if (postSum.getType() != 2) {
                logger.error("[图文垃圾数据], weixinAppid:{} mediaId:{}", weixinAppid, mediaId);
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
    private List<PostPO> wrapWeiXinPosts(String accessToken, List<PostPO> posts) throws Exception {
        List<PostPO> wxPosts = new ArrayList<>();

        for (PostPO postPO : posts) {
            PostPO wxPost = new PostPO();
            wxPost.setTitle(postPO.getTitle());
            wxPost.setThumbMediaId(postPO.getThumbMediaId());
            wxPost.setAuthor(postPO.getAuthor());
            wxPost.setDigest(postPO.getDigest());
            wxPost.setShowCoverPic(postPO.getShowCoverPic());
            wxPost.setContentSourceUrl(postPO.getContentSourceUrl());
            // 替换内容
            String replacedContent = replaceContentForUpload(accessToken, postPO.getContent());
            wxPost.setContent(replacedContent);
            // 设置顺序
            wxPost.setIndex(postPO.getIndex());

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
    private List<PostPO> cleanPosts(List<PostPO> posts, String accessToken) throws Exception {

        int index =0;
        short type = (short) (posts.size() > 1 ? 3 : 1);

        for (PostPO postPO : posts) {
            // 设置index顺序
            postPO.setIndex(index++);
            // 图文类型
            postPO.setType(type);
            // 上传图片
            String replacedContent = uploadContentImg(accessToken, postPO.getContent());
            // 过滤content中的js事件
            replacedContent = filterHtml(replacedContent);
            postPO.setContent(replacedContent);
            // 删除emoji
            postPO.setTitle(EmojiParser.removeAllEmojis(postPO.getTitle()));
            String digest = postPO.getDigest();
            if (digest != null) {
                digest = EmojiParser.removeAllEmojis(digest);
            }
            postPO.setDigest(digest);
            // 上传封面图片
            String thumbMediaId = postPO.getThumbMediaId();
            if (thumbMediaId == null || thumbMediaId.equals("")) {
                HashMap<String, String> retMap = WxPostManager.uploadImage(accessToken, postPO.getThumbUrl());
                thumbMediaId = retMap.get("mediaId");
            }
            postPO.setThumbMediaId(thumbMediaId);

            // 清除封面尺寸---"/imageView/v1/thumbnail"
            postPO.setThumbUrl(postPO.getThumbUrl().replaceAll("/imageView/v1/thumbnail.*$", ""));
            // 默认设置showCoverPic为0
            if (postPO.getShowCoverPic() == null){
                postPO.setShowCoverPic((short) 0);
            }
        }

        return posts;
    }


    /*** 清理微信图文数据 ***/
    private List<PostPO> cleanWxPosts(long weixinAppid, String mediaId, long updateTime, long userId, List<WxPostDTO> wxPostDTOs) throws Exception {
        int key = 0;
        List<PostPO> postPOList = new ArrayList<>();
        for (WxPostDTO wxPostDTO : wxPostDTOs) {

            PostPO postPO = new PostPO();

            String title = wxPostDTO.getTitle();
            if (title != null) {
                title = EmojiParser.removeAllEmojis(title);
            }
            postPO.setTitle(title);

            String digest = wxPostDTO.getDigest();
            if (digest != null) {
                digest = EmojiParser.removeAllEmojis(digest);
            }
            postPO.setDigest(digest);

            String author = wxPostDTO.getAuthor();
            if (author != null) {
                author = EmojiParser.removeAllEmojis(author);
            }
            postPO.setAuthor(author);

            postPO.setThumbMediaId(wxPostDTO.getThumbMediaId());
            postPO.setShowCoverPic(wxPostDTO.getShowCoverPic());
            postPO.setPostUrl(wxPostDTO.getUrl().replaceAll("&chksm=[^&]+", ""));
            postPO.setContentSourceUrl(wxPostDTO.getContentSourceUrl() == null ? "" : wxPostDTO.getContentSourceUrl());
            // 内容替换图片及视频
            String content = replacePicUrlFromWeixinPost(wxPostDTO.getContent(), userId);
            postPO.setContent(replaceVideoFromWeixinPost(content));
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
            postPO.setThumbUrl(picUrl);

            postPO.setWeixinAppid(weixinAppid);
            postPO.setMediaId(mediaId);
            postPO.setUpdateTime((int) updateTime);

            postPOList.add(postPO);
            key++;
        }
        // 继续清理数据
        return cleanPosts(postPOList, accountService.getAccessToken(weixinAppid));
    }


    /**
     * 把校验后的多图文新增到数据库, 必须先经过cleanPosts清洗数据
     */
    private void saveMultiPosts(long weixinAppid, List<PostPO> posts) {

        // 1. 单图文 2. 多图文总记录 3. 多图文中的一条
        short type = (short) (posts.size() > 1 ? 3 : 1);

        int index = 0;
        for (PostPO postPO : posts) {

            // 生成pageId
            postPO.setPageId(genPageId());
            // 先把content保存到mongo
            mongoPostDao.upsertPost(postPO.getPageId(), postPO.getContent());

            postPO.setWeixinAppid(weixinAppid);
            postPO.setType(type);
            postPO.setIndex(index++);

            postDao.insertPost(postPO);
        }

        // 如果是多图文，生成一条type为2的图文总记录
        if (posts.size() > 1) {

            PostPO sumPost = new PostPO();
            // 有效数据
            sumPost.setWeixinAppid(weixinAppid);
            sumPost.setPageId(genPageId());
            sumPost.setType((short) 2);
            sumPost.setIndex(0);
            sumPost.setMediaId(posts.get(0).getMediaId());
            sumPost.setUpdateTime(posts.get(0).getUpdateTime());
            // 把title拼接起来，保存多图文总记录里面
            StringBuilder sumTitle = new StringBuilder();
            for (PostPO postPO : posts) {
                sumTitle.append(postPO.getTitle());
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
                    wxSrc = uploadImageForPost(accessToken, wxSrc);
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
                        String wxSrc = uploadImageForPost(accessToken, src);
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
                        uploadImageForPost(accessToken, matcher.group(2)) + matcher.group(3)
        );

        // TODO: 对中转链接的替换； 校验上传成功的微信图片，肯定是小于1M的。
        return content;
    }

    @Override
    public void syncWeixinPosts(long weixinAppid, long userId) {
        if (! redisPostDao.couldSyncWxPost(weixinAppid)) {
            throw new BusinessException(ErrorCode.SYNC_WX_POST_TOO_OFTEN_ERROR);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("weixinAppid", weixinAppid);
        map.put("uid", userId);
        mqUtil.publish(MqConstant.IMPORT_WEIXIN_POST_LIST, map);
    }

    @Override
    public void calSyncWeixinPosts(long weixinAppid, long userId) {
        int fetchCount = 20;
        String accessToken = accountService.getAccessToken(weixinAppid);

        WxPostListDTO postListDTO = WxPostManager.getWxPostList(accessToken, 0, fetchCount);
        int total = postListDTO.getTotalCount();
        int index = postListDTO.getItemCount();

        for (WxPostListDTO.Item item: postListDTO.getItems()) {
            List<WxPostDTO> posts = item.getContent().getNewsItems();
            publishSyncWeixinPost(posts, item.getMediaId(), item.getUpdateTime(), weixinAppid, userId);
        }

        while (index < total) {
            postListDTO = WxPostManager.getWxPostList(accessToken, index, fetchCount);
            index += postListDTO.getItemCount();
            for (WxPostListDTO.Item item: postListDTO.getItems()) {
                List<WxPostDTO> posts = item.getContent().getNewsItems();
                publishSyncWeixinPost(posts, item.getMediaId(), item.getUpdateTime(), weixinAppid, userId);
            }
        }

    }

    /*** 判断是否应该同步微信图文，并发布mq消息 ***/
    private void publishSyncWeixinPost(List<WxPostDTO> wxPostDTOs, String mediaId, int updateTime, long weixinAppid, long userId) {
        PostPO postPO = getPostByMediaId(weixinAppid, mediaId);
        // 需要新增或者需要更新
        if (postPO == null || updateTime - postPO.getUpdateTime() > 2) {
            Map<String, Object> message = new HashMap<>();
            message.put("userId", userId);
            message.put("weixinAppid", weixinAppid);
            message.put("mediaId", mediaId);
            message.put("updateTime", updateTime);
            message.put("wxPostDTOs", JsonUtil.bean2String(wxPostDTOs));
            message.put("isNew", postPO == null);
            mqUtil.publish(MqConstant.IMPORT_WEIXIN_POST, message);
        }
    }


    @Override
    public void importWeixinPost(long weixinAppid, String mediaId, long updateTime, long userId, List<WxPostDTO> wxPostDTOs) throws Exception {
        List<PostPO> postPOList = cleanWxPosts(weixinAppid, mediaId, updateTime, userId, wxPostDTOs);
        // 入库
        saveMultiPosts(weixinAppid, postPOList);
    }

    @Override
    public void updateWeixinPost(long weixinAppid, String mediaId, long updateTime, long userId, List<WxPostDTO> wxPostDTOs) throws Exception {
        List<PostPO> postPOList = cleanWxPosts(weixinAppid, mediaId, updateTime, userId, wxPostDTOs);

        // 修改数据库
        PostPO postPO = getPostByMediaId(weixinAppid, mediaId);
        // 多图文情况
        List<PostPO> multiPosts = new ArrayList<>();
        if (postPO.getType() == 2) {
            multiPosts = listMultiPosts(weixinAppid, mediaId, false);
        }

        // 再次校验修改时间，以免覆盖在平台的修改
        if (updateTime - postPO.getUpdateTime() > 2) {
            // 现在是单图文
            if (postPOList.size() == 1) {
                PostPO newPost = postPOList.get(0);
                newPost.setUpdateTime((int) updateTime);
                newPost.setPageId(postPO.getPageId());
                // 更新内容
                mongoPostDao.upsertPost(newPost.getPageId(), newPost.getContent());
                postDao.updatePost(newPost, newPost.getPageId());

                // 清除其他图文
                for (PostPO multiPost: multiPosts) {
                    postDao.deletePostByPageId(multiPost.getPageId());
                }
            }
            // 现在是多图文
            else {
                StringBuilder sumTitle = new StringBuilder();
                for (int i = 0; i < postPOList.size(); i++) {
                    PostPO newPost = postPOList.get(i);
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
                        newPost.setPageId(genPageId());
                        mongoPostDao.upsertPost(newPost.getPageId(), newPost.getContent());
                        postDao.insertPost(newPost);
                    }
                }
                // 删除多余图文
                for (int i = postPOList.size(); i < multiPosts.size(); i++) {
                    postDao.deletePostByPageId(multiPosts.get(i).getPageId());
                }
                // 修改图文总记录
                postPO.setWeixinAppid(weixinAppid);
                postPO.setType((short) 2);
                postPO.setIndex(0);
                postPO.setMediaId(mediaId);
                postPO.setUpdateTime((int) updateTime);
                postPO.setTitle(sumTitle.toString());
                postPO.setThumbMediaId("");
                postPO.setThumbUrl("");
                postPO.setAuthor("");
                postPO.setDigest("");
                postPO.setPostUrl("");
                postPO.setContentSourceUrl("");
                postPO.setShowCoverPic((short) 0);
                postDao.updatePost(postPO, postPO.getPageId());
            }
        }
    }

    @Override
    public HashMap<String, String> uploadWxMaterial(long weixinAppid, String imgUrl) {
        try {
            return WxPostManager.uploadImage(accountService.getAccessToken(weixinAppid), imgUrl);
        } catch (DownloadFileFailedException e) {
            logger.warn("[Post:uploadWxMaterial] download file failed, imgUrl:{}", imgUrl, e);
            throw new BusinessException(ErrorCode.OPERATION_FAILED, "下载图片失败，请稍后重试");
        }
    }

    @Override
    public String uploadImageForPost(long weixinAppid, String imgUrl) {
        String accessToken = accountService.getAccessToken(weixinAppid);
        return uploadImageForPost(accessToken, imgUrl);
    }

    /**
     * 使用accessToke上传图文中的图片
     */
    private String uploadImageForPost(String accessToken, String imgUrl) {
        imgUrl = UrlUtil.fixQuote(imgUrl);
        imgUrl = UrlUtil.fixProtocol(imgUrl);

        // 本来就是微信url的不再上传
        if (imgUrl.indexOf("https://mmbiz") == 0 || imgUrl.indexOf("http://mmbiz") == 0) {
            return imgUrl;
        }

        // 先从redis中取
        String wxUrl = redisImageDao.getImageUrl(imgUrl);
        if (wxUrl != null) {
            return wxUrl;
        }

        try {
            wxUrl = WxPostManager.uploadImgForPost(accessToken, imgUrl);
        } catch (DownloadFileFailedException e) {
            logger.warn("[Post:uploadImageForPost] download file failed, imgUrl:{}", imgUrl, e);
            throw new BusinessException(ErrorCode.OPERATION_FAILED, "下载图片失败，请稍后重试");
        }

        redisImageDao.setImageUrl(imgUrl, wxUrl);
        return wxUrl;
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
            return KzManager.uploadPicToKz(kzPicUrl, userId);
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
