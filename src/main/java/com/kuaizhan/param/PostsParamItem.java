package com.kuaizhan.param;

import com.kuaizhan.pojo.DO.PostDO;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by zixiong on 2017/4/12.
 */
public class PostsParamItem {

    @Size(min = 1, max = 64, message = "图文的标题不能为空或者只含有空格（标题会过滤emoji表情等特殊字符")
    private String title;

    @Size(max = 8, message = "作者的长度不能超过8个字符")
    private String author;

    @Size(max = 120, message = "摘要不能超过120个字符")
    private String digest;

    @NotNull(message = "封面图不能为空")
    @NotBlank(message = "封面图不能为空")
    private String thumbUrl;

    @NotNull(message = "内容不能为空")
    @NotBlank(message = "内容不能为空")
    private String content;

    private String thumbMediaId;
    private String contentSourceUrl;
    private Short showCoverPic;

    private Long pageId;
    private String mediaId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public String getContentSourceUrl() {
        return contentSourceUrl;
    }

    public void setContentSourceUrl(String contentSourceUrl) {
        this.contentSourceUrl = contentSourceUrl;
    }

    public Short getShowCoverPic() {
        return showCoverPic;
    }

    public void setShowCoverPic(Short showCoverPic) {
        this.showCoverPic = showCoverPic;
    }

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }


    public PostDO toPostDO() {
        PostDO postDO = new PostDO();

        postDO.setTitle(title);
        postDO.setAuthor(author);
        postDO.setDigest(digest);
        postDO.setContent(content);
        postDO.setThumbMediaId(thumbMediaId);
        postDO.setThumbUrl(thumbUrl);
        postDO.setContentSourceUrl(contentSourceUrl);
        postDO.setShowCoverPic(showCoverPic);
        postDO.setPageId(pageId);
        postDO.setMediaId(mediaId);

        return postDO;
    }
}
