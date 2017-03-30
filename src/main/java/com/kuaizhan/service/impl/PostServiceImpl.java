package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.mapper.PostDao;
import com.kuaizhan.exception.business.MaterialDeleteException;
import com.kuaizhan.dao.mongo.MongoPostDao;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.PostService;
import com.kuaizhan.service.WeixinPostService;
import com.kuaizhan.utils.ReplaceCallbackMatcher;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    MongoPostDao mongoPostDao;


    @Resource
    AccountService accountService;

    @Override
    public Page<PostDO> listPostsByPagination(long weixinAppid, Integer page) throws DaoException {

        Page<PostDO> postDOPage = new Page<>(page, ApplicationConfig.PAGE_SIZE_LARGE);

        try {
            List<PostDO> posts = postDao.listPostsByPagination(weixinAppid, postDOPage);
            postDOPage.setResult(posts);

            long totalCount = postDao.count(weixinAppid);
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
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return multiPosts;
    }

    @Override
    public void deletePost(long weixinAppid, long pageId, String accessToken) throws DaoException, MaterialDeleteException {

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
    public PostDO getPostByPageId(long pageId) throws DaoException {
        try {
            return postDao.getPost(pageId);
        } catch (Exception e) {
            throw new DaoException(e);
        }

    }

    public void insertMultiPosts(long weixinAppid, List<PostDO> posts) throws Exception {

        AccountDO accountDO = accountService.getAccountByWeixinAppId(weixinAppid);
        // 在方法执行过程中，有失效的风险
        String accessToken = accountDO.getAccessToken();

        // 保存数据库前对图文数据做预处理
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

        // 上传到微信

        // 对内容中的图片进一步处理
        // 封装上传微信的PostDO list
        List<PostDO> wxPosts = new ArrayList<>();
        for (PostDO postDO: posts) {
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

        // 上传到数据库
        // 1. 单图文 2. 多图文总记录 3. 多图文中的一条
        short type = (short) (posts.size() > 1? 3 : 1);
        int index = 0;
        for (PostDO postDO: posts){
            // 先把content保存到mongo
            long pageId = mongoPostDao.insertPost(postDO.getContent());
            postDO.setPageId(pageId);
            postDO.setMediaId(mediaId);
            postDO.setWeixinAppid(weixinAppid);
            postDO.setType(type);
            postDO.setIndex(index++);

            System.out.println(postDO);
            postDao.insertPost(postDO);
        }

        // 如果是多图文，生成一条type为3的总数据
        if (posts.size() > 1) {

            PostDO sumPost = new PostDO();
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
            postDao.insertPost(sumPost);
        }
    }

    /**
     * 删除html中的js标签，on_click事件
     * @param html
     * @return
     * @throws Exception
     */
    public String filterHtml(String html) throws Exception {
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
    public String uploadContentImg(final String accessToken, String content) throws Exception {

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
     * @param content
     * @return
     */
    public String replaceContentForUpload(String content) throws Exception {
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
