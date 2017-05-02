package com.kuaizhan.pojo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 获取微信图文的单篇图文DTO
 * 接口文档: https://mp.weixin.qq.com/wiki/12/3c12fac7c14cb4d0e0d4fe2fbc87b638.html
 * Created by zixiong on 2017/4/25.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WxPostDTO {

    @JsonProperty("title")
    private String title;

    @JsonProperty("thumb_media_id")
    private String thumbMediaId;

    @JsonProperty("thumb_url")
    private String thumbUrl;

    @JsonProperty("show_cover_pic")
    private Short showCoverPic;

    @JsonProperty("author")
    private String author;

    @JsonProperty("digest")
    private String digest;

    @JsonProperty("content")
    private String content;

    @JsonProperty("url")
    private String url;

    @JsonProperty("content_source_url")
    private String contentSourceUrl;
}
