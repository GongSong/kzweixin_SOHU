package com.kuaizhan.kzweixin.service.impl;

import com.google.common.collect.ImmutableList;
import com.kuaizhan.kzweixin.config.ApplicationConfig;
import com.kuaizhan.kzweixin.config.KzApiConfig;
import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.constant.MqConstant;
import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.constant.PostConstant;
import com.kuaizhan.kzweixin.dao.mapper.PostDao;
import com.kuaizhan.kzweixin.cache.ImageCache;
import com.kuaizhan.kzweixin.cache.PostCache;
import com.kuaizhan.kzweixin.dao.mapper.auto.GuideFollowPostMapper;
import com.kuaizhan.kzweixin.dao.po.auto.AccountPO;
import com.kuaizhan.kzweixin.dao.po.auto.GuideFollowPostPO;
import com.kuaizhan.kzweixin.exception.BusinessException;
import com.kuaizhan.kzweixin.exception.common.DownloadFileFailedException;
import com.kuaizhan.kzweixin.exception.kuaizhan.Export2KzException;
import com.kuaizhan.kzweixin.exception.kuaizhan.GetKzArticleException;
import com.kuaizhan.kzweixin.dao.mongo.MongoPostDao;
import com.kuaizhan.kzweixin.exception.kuaizhan.KZPicUploadException;
import com.kuaizhan.kzweixin.exception.post.GuideFollowPostNotFoundException;
import com.kuaizhan.kzweixin.exception.weixin.*;
import com.kuaizhan.kzweixin.manager.KzManager;
import com.kuaizhan.kzweixin.manager.WxPostManager;
import com.kuaizhan.kzweixin.manager.db.PostDBManager;
import com.kuaizhan.kzweixin.mq.dto.ArticleImportDTO;
import com.kuaizhan.kzweixin.mq.dto.SyncWxPostDTO;
import com.kuaizhan.kzweixin.mq.dto.SyncWxPostListDTO;
import com.kuaizhan.kzweixin.dao.po.PostPO;
import com.kuaizhan.kzweixin.entity.post.ArticleDTO;
import com.kuaizhan.kzweixin.entity.common.Page;
import com.kuaizhan.kzweixin.entity.post.WxPostListDTO;
import com.kuaizhan.kzweixin.entity.post.WxPostDTO;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.PostService;
import com.kuaizhan.kzweixin.utils.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by zixiong on 2017/3/20.
 */
@Service
public class PostServiceImpl implements PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Resource
    private PostDao postDao;
    @Resource
    private PostCache postCache;
    @Resource
    private PostDBManager postDBManager;
    @Resource
    private ImageCache imageCache;
    @Resource
    private MqUtil mqUtil;
    @Resource
    private MongoPostDao mongoPostDao;
    @Resource
    private AccountService accountService;
    @Resource
    private GuideFollowPostMapper guideFollowPostMapper;

    @Override
    public Page<PostPO> listPostsByPage(long weixinAppid, String title, Integer pageNum, Boolean flat) {

        Page<PostPO> page = new Page<>(pageNum, AppConstant.PAGE_SIZE_MIDDLE);

        List<PostPO> posts;
        posts = postDao.listPostsByPage(weixinAppid, title, page, flat);

        long totalCount = postDao.count(weixinAppid, title, flat);
        page.setTotalCount(totalCount);
        page.setResult(posts);

        return page;
    }

    @Override
    public List<PostPO> getMultiPosts(long weixinAppid, String mediaId, Boolean withContent) {

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
    public void deletePost(long weixinAppid, long pageId) throws WxPostUsedException {

        PostPO postPO = getPostByPageId(pageId);

        if (postPO != null) {
            String mediaId = postPO.getMediaId();
            //微信删除
            String accessToken = accountService.getAccessToken(weixinAppid);
            WxPostManager.deletePost(mediaId, accessToken);
            //数据库删除
            postDao.deletePost(weixinAppid, mediaId);
        }
    }

    @Override
    public Boolean existPost(long weixinAppid, String mediaId) {
        return postDao.existPost(weixinAppid, mediaId);
    }

    @Override
    public PostPO getPostByPageId(long pageId) {

        PostPO postPO = postDao.getPost(pageId);
        if (postPO != null) {
            postPO.setContent(getPostContent(pageId));
        }
        return postPO;
    }

    @Override
    public List<PostPO> getPostsByPageId(List<Long> pageIds) {
        List<PostPO> postPOS = new ArrayList<>();
        for(Long id : pageIds) {
            PostPO postPO = postDao.getPost(id);
            if (postPO != null) {
                postPOS.add(postPO);
            }
        }
        return postPOS;
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
        return KzManager.getKzArticle(pageId);
    }

    @Override
    public void asyncImportKzArticles(long weixinAppid, List<Long> pageIds) {
        if (pageIds.size() > 0) {
            ArticleImportDTO dto = new ArticleImportDTO();
            dto.setWeixinAppid(weixinAppid);
            dto.setPageIds(pageIds);
            mqUtil.publish(MqConstant.IMPORT_KUAIZHAN_POST, JsonUtil.bean2String(dto));
        }
    }

    public String getPostMediaId(long pageId) {
        PostPO postPO = postDao.getPost(pageId);
        if (postPO != null) {
            return postPO.getMediaId();
        }
        return null;
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
                    List<PostPO> multiPosts = getMultiPosts(weixinAppid, postPO.getMediaId(), false);
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
    public long addMultiPosts(long weixinAppid, List<PostPO> posts)  {

        // 在方法执行过程中，有失效的风险
        String accessToken = accountService.getAccessToken(weixinAppid);

        // 对图文数据做预处理
        posts = cleanPosts(posts, accessToken);

        // 上传到微信
        String mediaId = WxPostManager.uploadPosts(accessToken, wrapWxPosts(accessToken, posts));
        for (PostPO postPO : posts) {
            postPO.setMediaId(mediaId);
        }

        // 入库, 调用proxy类才能执行事务
        return postDBManager.saveMultiPosts(weixinAppid, posts);
    }

    @Override
    public void updateMultiPosts(long weixinAppid, long pageId, List<PostPO> posts) {

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
            oldPosts = getMultiPosts(weixinAppid, oldPost.getMediaId(), true);
        }

        // 数目不对应, 接口调用错误
        if (oldPosts.size() != posts.size()) {
            throw new BusinessException(ErrorCode.CAN_NOT_CHANGE_POST_NUM);
        }

        String accessToken = accountService.getAccessToken(weixinAppid);

        // 对图文数据做预处理
        posts = cleanPosts(posts, accessToken);
        // 更新到微信的图文数据
        List<PostPO> wxPosts = wrapWxPosts(accessToken, posts);

        // 修改微信后台文章
        String mediaId = oldPost.getMediaId();

        int curIndex = 0;
        int curUpdateTime = 0;
        try {
            for (int index = 0; index < wxPosts.size(); index++) {
                curIndex = index;
                if (! wxPostEqual(oldPosts.get(index), posts.get(index))) {
                    WxPostManager.updatePost(accessToken, oldPost.getMediaId(), wxPosts.get(index));
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

        postDBManager.updateMultiPosts(weixinAppid, oldPost.getMediaId(), posts);
    }

    /**
     * 比较两个post上传给微信的字段是否不同
     */
    private boolean wxPostEqual(PostPO postPO1, PostPO postPO2) {
        if (!Objects.equals(postPO1.getTitle(), postPO2.getTitle())
                || !Objects.equals(postPO1.getThumbMediaId(), postPO2.getThumbMediaId())
                || !Objects.equals(postPO1.getAuthor(), postPO2.getAuthor())
                || !Objects.equals(postPO1.getDigest(), postPO2.getDigest())
                || !Objects.equals(postPO1.getShowCoverPic(), postPO2.getShowCoverPic())
                || !Objects.equals(postPO1.getContentSourceUrl(), postPO2.getContentSourceUrl())
                || !Objects.equals(postPO1.getContent(), postPO2.getContent())) {
            return false;
        }
        return true;
    }



    /**
     * 把存数据库的多图文DO对象，封装成同步给微信的。
     * 替换了内容中的图片url为wx_src
     */
    private List<PostPO> wrapWxPosts(String accessToken, List<PostPO> posts) {
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
            String replacedContent = getWxContent(accessToken, postPO.getContent());
            wxPost.setContent(replacedContent);
            // 设置顺序
            wxPost.setIndex(postPO.getIndex());

            wxPosts.add(wxPost);
        }
        return wxPosts;
    }

    /**
     * 保存前，对图文数据做预处理
     * 上传内容中的图片到微信、上传封面图片；替换emoji, 替换js
     */
    private List<PostPO> cleanPosts(List<PostPO> posts, String accessToken) {

        int index =0;
        short type = (short) (posts.size() > 1 ? 3 : 1);

        for (PostPO postPO : posts) {
            // 设置index顺序
            postPO.setIndex(index++);
            // 图文类型
            postPO.setType(type);

            /* 清理title */
            postPO.setTitle(StrUtil.removeEmojis(postPO.getTitle()));

            /* 清理digest */
            String digest = postPO.getDigest();
            digest = StrUtil.removeEmojis(digest);
            digest = StrUtil.chopStr(digest, PostConstant.DIGEST_MAX);
            postPO.setDigest(digest);

            /* 检查contentSourceUrl */
            String contentSourceUrl = postPO.getContentSourceUrl();
            postPO.setContentSourceUrl(StrUtil.chopStr(contentSourceUrl, PostConstant.CONTENT_SOURCE_URL_MAX));

            /* 清理content */
            // 上传图片
            String content = uploadContentImg(accessToken, postPO.getContent());
            // 过滤content中的js事件
            content = filterHtml(content);
            postPO.setContent(content);

            // 上传封面图片
            String thumbMediaId = postPO.getThumbMediaId();
            if (thumbMediaId == null || thumbMediaId.equals("")) {
                HashMap<String, String> retMap = WxPostManager.uploadMaterial(accessToken, postPO.getThumbUrl());
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


    /*** 清理微信返回的图文数据 ***/
    private List<PostPO> cleanWxPosts(long weixinAppid, String mediaId, long updateTime, List<WxPostDTO> wxPostDTOs) {

        List<PostPO> posts = new ArrayList<>();

        for (WxPostDTO wxPostDTO : wxPostDTOs) {
            PostPO postPO = new PostPO();
            postPO.setTitle(StrUtil.removeEmojis(wxPostDTO.getTitle()));

            String digest = wxPostDTO.getDigest();
            digest = StrUtil.removeEmojis(digest);
            digest = StrUtil.chopStr(digest, PostConstant.DIGEST_MAX);
            postPO.setDigest(digest);

            postPO.setAuthor(wxPostDTO.getAuthor());
            postPO.setThumbMediaId(wxPostDTO.getThumbMediaId());
            postPO.setShowCoverPic(wxPostDTO.getShowCoverPic());
            postPO.setPostUrl(wxPostDTO.getUrl().replaceAll("&chksm=[^&]+", ""));
            postPO.setContentSourceUrl(StrUtil.chopStr(wxPostDTO.getContentSourceUrl(), PostConstant.CONTENT_SOURCE_URL_MAX));
            postPO.setWeixinAppid(weixinAppid);
            postPO.setMediaId(mediaId);
            postPO.setUpdateTime((int) updateTime);

            // 内容清理
            postPO.setContent(cleanWxPostContent(wxPostDTO.getContent()));

            // 缩略图链接替换
            String picUrl = wxPostDTO.getThumbUrl();
            if (picUrl == null || "".equals(picUrl)) {
                picUrl = KzApiConfig.getResUrl("/res/weixin/images/post-default-cover-900-500.png");
            }
            postPO.setThumbUrl(getKzImageUrl(picUrl));

            posts.add(postPO);
        }

        // 继续清理数据
        int index =0;
        short type = (short) (posts.size() > 1 ? 3 : 1);
        for (PostPO postPO : posts) {
            // 设置index顺序
            postPO.setIndex(index++);
            // 图文类型
            postPO.setType(type);
        }

        return posts;
    }



    /**
     * 删除html中的js标签，on_click事件
     */
    private String filterHtml(String html) {
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
    private String uploadContentImg(final String accessToken, String content) {
        // 对wx_src垃圾数据进行清理，即wx_src标签下的内容不是微信链接。
        String wxSrcRegex = "(wx_src=)[\"'](?<wxSrc>[^\"']+?)[\"']";
        ReplaceCallbackMatcher callbackMatcher = new ReplaceCallbackMatcher(wxSrcRegex);

        content = callbackMatcher.replaceMatches(content,
                matcher -> {
                    String wxSrc = matcher.group("wxSrc");
                    wxSrc = getWxImageUrl(accessToken, wxSrc);
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
                        String wxSrc = getWxImageUrl(accessToken, src);
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
    private String getWxContent(final String accessToken, String content) {

        // 用微信wx_src替换src
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
                        getWxImageUrl(accessToken, matcher.group(2)) + matcher.group(3)
        );

        // TODO: 对中转链接的替换； 校验上传成功的微信图片，肯定是小于1M的。
        return content;
    }

    @Override
    public void syncWeixinPosts(long weixinAppid) {
        if (! postCache.couldSyncWxPost(weixinAppid)) {
            throw new BusinessException(ErrorCode.SYNC_WX_POST_TOO_OFTEN_ERROR);
        }

        SyncWxPostListDTO dto = new SyncWxPostListDTO();
        dto.setWeixinAppid(weixinAppid);
        mqUtil.publish(MqConstant.IMPORT_WEIXIN_POST_LIST, JsonUtil.bean2String(dto));
    }

    @Override
    public void calSyncWeixinPosts(long weixinAppid) {
        int fetchCount = 20;
        String accessToken = accountService.getAccessToken(weixinAppid);

        WxPostListDTO postListDTO = WxPostManager.getWxPostList(accessToken, 0, fetchCount);
        int total = postListDTO.getTotalCount();
        int index = postListDTO.getItemCount();

        for (WxPostListDTO.Item item: postListDTO.getItems()) {
            List<WxPostDTO> posts = item.getContent().getNewsItems();
            publishSyncWeixinPost(posts, item.getMediaId(), item.getUpdateTime(), weixinAppid);
        }

        while (index < total) {
            postListDTO = WxPostManager.getWxPostList(accessToken, index, fetchCount);
            index += postListDTO.getItemCount();
            for (WxPostListDTO.Item item: postListDTO.getItems()) {
                List<WxPostDTO> posts = item.getContent().getNewsItems();
                publishSyncWeixinPost(posts, item.getMediaId(), item.getUpdateTime(), weixinAppid);
            }
        }

    }

    @Override
    public void importWeixinPost(long weixinAppid, String mediaId, long updateTime, List<WxPostDTO> wxPostDTOs) {
        List<PostPO> postPOList = cleanWxPosts(weixinAppid, mediaId, updateTime, wxPostDTOs);
        // 入库
        postDBManager.saveMultiPosts(weixinAppid, postPOList);
    }

    @Override
    public void updateWeixinPost(long weixinAppid, String mediaId, long updateTime, List<WxPostDTO> wxPostDTOs) {
        List<PostPO> postPOList = cleanWxPosts(weixinAppid, mediaId, updateTime, wxPostDTOs);

        // 修改数据库
        PostPO postPO = getPostByMediaId(weixinAppid, mediaId);
        // 多图文情况
        List<PostPO> multiPosts = new ArrayList<>();
        if (postPO.getType() == 2) {
            multiPosts = getMultiPosts(weixinAppid, mediaId, false);
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
                        newPost.setPageId(postDBManager.genPageId());
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
        return WxPostManager.uploadMaterial(accountService.getAccessToken(weixinAppid), imgUrl);
    }

    @Override
    public String uploadWxImage(long weixinAppid, String imgUrl) {
        String accessToken = accountService.getAccessToken(weixinAppid);
        return getWxImageUrl(accessToken, imgUrl);
    }

    @Override
    public String getGuideFollowPost(long weixinAppid) throws GuideFollowPostNotFoundException {
        String url = postCache.getGuideFollowPostUrl(weixinAppid);

        // 未命中缓存
        if (url == null) {
            GuideFollowPostPO guideFollowPost = guideFollowPostMapper.selectByPrimaryKey(weixinAppid);
            if (guideFollowPost == null || guideFollowPost.getStatus() != 1) {
                throw new GuideFollowPostNotFoundException();
            }

            url = guideFollowPost.getPostUrl();
            postCache.setGuideFollowPostUrl(weixinAppid, url);
        }

        return url;
    }

    @Override
    public String addGuideFollowPost(long weixinAppid) {
        // 存在则不新建
        GuideFollowPostPO guideFollowPost = guideFollowPostMapper.selectByPrimaryKey(weixinAppid);
        if (guideFollowPost != null && isPostValid(guideFollowPost.getPageId())) {
            return guideFollowPost.getPostUrl();
        }

        // 否则生成图文
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);

        PostPO postPO = new PostPO();
        postPO.setTitle("点击蓝字【\"" + accountPO.getNickName() + "\"】关注公众号，查看更多精彩内容");
        postPO.setThumbUrl("http://pic.kuaizhan.com/g1/M01/57/DA/CgpQU1acTGWAHRwMAAFxhsY6vio7105783");
        postPO.setContent("<p><img src=\"//pic.kuaizhan.com/g1/M00/5E/1E/wKjmqVagjMuAA0_hAAE8xMKaU1w2704850\" " +
                "wx_src=\"https://mmbiz.qlogo.cn/mmbiz/" +
                "V09zMGOnzfXdl2LXialUnjk0Av5LYjDlegrW1efMq63ibmmUd5nAoOia5lpicmEzibYMgIiccEMWse4cbreU234xhQkg/0" +
                "?wx_fmt=png\"/></p>");

        long pageId = addMultiPosts(weixinAppid, ImmutableList.of(postPO));
        String postUrl = getPostWxUrl(weixinAppid, pageId);

        // 保存引导关注图文
        GuideFollowPostPO record = new GuideFollowPostPO();
        record.setWeixinAppid(weixinAppid);
        record.setPageId(pageId);
        record.setPostUrl(postUrl);
        record.setStatus(1);
        record.setCreateTime(DateUtil.curSeconds());
        record.setUpdateTime(DateUtil.curSeconds());
        // 不知道当初设计存这两个字段的目的是什么
        record.setNickName(accountPO.getNickName());
        record.setHeadImg(accountPO.getHeadImg());
        guideFollowPostMapper.upsert(record);

        // 清理缓存
        postCache.deleteGuideFollowPostUrl(weixinAppid);

        return postUrl;
    }


    /**
     * 判断一篇图文url是否有效
     */
    private boolean isPostValid(long pageId) {
        PostPO postPO = postDao.getPost(pageId);
        String accessToken = accountService.getAccessToken(postPO.getWeixinAppid());
        try {
            WxPostManager.getWxPost(postPO.getMediaId(), accessToken);
        } catch (WxPostDeletedException e) {
            return false;
        }
        return true;
    }

    /**
     *  判断是否应该同步微信图文，并发布mq消息
     */
    private void publishSyncWeixinPost(List<WxPostDTO> wxPostDTOs, String mediaId, int updateTime, long weixinAppid) {
        PostPO postPO = getPostByMediaId(weixinAppid, mediaId);
        // 需要新增或者需要更新
        if (postPO == null || updateTime - postPO.getUpdateTime() > 2) {

            SyncWxPostDTO dto = new SyncWxPostDTO();
            dto.setWeixinAppid(weixinAppid);
            dto.setMediaId(mediaId);
            dto.setUpdateTime(updateTime);
            dto.setWxPostDTOS(wxPostDTOs);
            dto.setIsNew(postPO == null);
            mqUtil.publish(MqConstant.IMPORT_WEIXIN_POST, JsonUtil.bean2String(dto));
        }
    }

    /**
     * 获取图片的微信url
     */
    private String getWxImageUrl(String accessToken, String imgUrl)
            throws DownloadFileFailedException,
            WxInvalidImageFormatException,
            WxMediaSizeOutOfLimitException{

        imgUrl = UrlUtil.fixQuote(imgUrl);
        imgUrl = UrlUtil.fixProtocol(imgUrl);

        // 本来就是微信url的不再上传
        if (imgUrl.indexOf("https://mmbiz") == 0 || imgUrl.indexOf("http://mmbiz") == 0) {
            return imgUrl;
        }

        // 先从redis中取
        String wxUrl = imageCache.getImageUrl(imgUrl);
        if (wxUrl != null) {
            return wxUrl;
        }

        wxUrl = WxPostManager.uploadImage(accessToken, imgUrl);
        imageCache.setImageUrl(imgUrl, wxUrl);

        return wxUrl;
    }


    /*** 微信图片地址生成快站图片url，若失败则返回原url ***/
     private String getKzImageUrl(String imgUrl) {
        // 若不是微信图片则返回原url
        if (!imgUrl.contains("mmbiz")) return imgUrl;

        // 去掉微信图片中的tp=webp
         String kzPicUrl = imgUrl;
         kzPicUrl = kzPicUrl.replaceAll("&tp=webp", "");
         kzPicUrl = kzPicUrl.replaceAll("\\?tp=webp", "");
         // 去掉所有&amp;后的内容
         kzPicUrl = kzPicUrl.replaceAll("&amp;.*$", "");

        try {
            String url = KzManager.uploadPicToKz(kzPicUrl);
            imageCache.setImageUploaded(url);
            return url;
        } catch (KZPicUploadException e) {
            logger.warn("[getKzImageUrl] upload kz image failed, url: {}", kzPicUrl, e);
        }

        // 上传失败，返回原始url
        return imgUrl;
    }

    /**
     * 清理微信内容
     * 替换微信内容中的微信图片链接为快站链接
     * 替换微信图文中视频链接
     */
    private String cleanWxPostContent(String content) {
        // 将文章中的图片的data-src替换为src
        content = content.replaceAll("(<img [^>]*)(data-src)", "$1src");

        // img中的微信图片转成快站链接,记录wx_src
        String regex = "(<img[^>]* src=[\"'])([^\"']+?)([\"'][^>]*>)";
        ReplaceCallbackMatcher replaceCallbackMatcher = new ReplaceCallbackMatcher(regex);
        content = replaceCallbackMatcher.replaceMatches(content,
                matcher -> matcher.group(1)
                        + getKzImageUrl(matcher.group(2))
                        + "\" wx_src=\""
                        + matcher.group(2)
                        + matcher.group(3)
        );

        // 背景图中的微信图片转成快站链接
        regex = "url\\(\"?'?(?:&quot;)?(https?:\\/\\/mmbiz[^)]+?)(?:&quot;)?\"?'?\\)";
        replaceCallbackMatcher = new ReplaceCallbackMatcher(regex);
        content = replaceCallbackMatcher.replaceMatches(content,
                matcher -> "url(" + getKzImageUrl(matcher.group(1)) + ")"
        );

        // 视频链接
        content = content.replaceAll(
                "<iframe[^>]*class=\"video_iframe\"[^>]*data-src=\"([^\"]+)vid=([^&]+)([^\"]+)\"[^>]*>",
                "<iframe style=\"z-index:1;\" class=\"video_iframe\" data-src=\"$1vid=$2$3\" " +
                        "frameborder=\"0\" allowfullscreen=\"\" " +
                        "src=\"https://v.qq.com/iframe/player.html?vid=$2&tiny=0&auto=0\" width=\"280px\" height=\"100%\">"
        );

        return content;

    }
}
