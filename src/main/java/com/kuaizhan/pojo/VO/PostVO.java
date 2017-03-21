package com.kuaizhan.pojo.VO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zixiong on 2017/3/21.
 */
public class PostVO {
    private Long pageId;
    private String title ;
    private String thumbUrl;
    private String author;
    private String digest;
    private Short type;
    private Integer updateTime;

    private List<PostVO> multiPosts = new ArrayList<PostVO>();  // 图文消息下面的多图文

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

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public List<PostVO> getMultiPosts() {
        return multiPosts;
    }

    public void setMultiPosts(List<PostVO> multiPosts) {
        this.multiPosts = multiPosts;
    }

    @Override
    public String toString() {
        return "PostVO{" +
                "pageId=" + pageId +
                ", title='" + title + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                ", author='" + author + '\'' +
                ", digest='" + digest + '\'' +
                ", type=" + type +
                ", updateTime=" + updateTime +
                '}';
    }
}
