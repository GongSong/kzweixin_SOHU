package com.kuaizhan.kzweixin.pojo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 快站文章业务对象
 * Created by liangjiateng on 2017/3/30.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleDTO {

    @JsonProperty("site_id")
    private Long siteId;
    @JsonProperty("page_url")
    private String pageUrl;
    @JsonProperty("pic_url")
    private String coverUrl;

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
}
