package com.kuaizhan.pojo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 快站文章业务对象
 * Created by liangjiateng on 2017/3/30.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleDTO {
    @JsonProperty("site_id")
    private Long siteId;
    @JsonProperty("page_url")
    private String pageUrl;

    private String title;
    @JsonProperty("tag_id")
    private Integer tagId;
    @JsonProperty("seo_keyword")
    private String seoKeyword;
    @JsonProperty("seo_description")
    private String seoDescription;
    @JsonProperty("create_time")
    private Long createTime;
    @JsonProperty("update_time")
    private Long updateTime;
    @JsonProperty("publish_time")
    private Long publishTime;

    private List<String> content;

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getSeoKeyword() {
        return seoKeyword;
    }

    public void setSeoKeyword(String seoKeyword) {
        this.seoKeyword = seoKeyword;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Long publishTime) {
        this.publishTime = publishTime;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ArticleDTO{" +
                "siteId=" + siteId +
                ", pageUrl='" + pageUrl + '\'' +
                ", title='" + title + '\'' +
                ", tagId=" + tagId +
                ", seoKeyword='" + seoKeyword + '\'' +
                ", seoDescription='" + seoDescription + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", publishTime=" + publishTime +
                ", content=" + content +
                '}';
    }
}
