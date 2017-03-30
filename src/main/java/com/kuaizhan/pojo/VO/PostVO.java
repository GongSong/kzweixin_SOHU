package com.kuaizhan.pojo.VO;

/**
 * Created by zixiong on 2017/3/21.
 */
public class PostVO {
    private Long pageId;
    private String mediaId;
    private String title ;
    private String author;
    private String digest;
    private String content;
    private String thumbUrl;
    private String thumbMediaId;
    private String contentSourceUrl;
    private Integer updateTime;

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
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

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
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

    @Override
    public String toString() {
        return "PostVO{" +
                "pageId=" + pageId +
                ", mediaId='" + mediaId + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", digest='" + digest + '\'' +
                ", content='" + content + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                ", thumbMediaId='" + thumbMediaId + '\'' +
                ", contentSourceUrl='" + contentSourceUrl + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
