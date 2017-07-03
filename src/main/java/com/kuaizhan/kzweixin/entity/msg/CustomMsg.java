package com.kuaizhan.kzweixin.entity.msg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 客服消息数据
 * Created by zixiong on 2017/5/31.
 */
public class CustomMsg {

    /**
     * 文本类型
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Text {
        @NotNull(message = "content can not be null")
        private String content;
    }

    /**
     * 图片类型
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Image {
        @JsonProperty("media_id")
        private String mediaId;

        @NotNull(message = "pic_url can not be null")
        @JsonProperty("pic_url")
        private String picUrl;
    }

    /**
     * 图文类型
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MpNews {
        @Size(min = 1, max = 8)
        private List<Long> posts = new ArrayList<>();
    }

    /**
     * 链接组类型
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class News {
        @Size(min = 1, max = 8)
        private List<Article> articles = new ArrayList<>();
    }

    /**
     * 链接组中的子对象
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Article {
        private String title;
        private String description;
        @JsonProperty("picurl")
        private String picUrl;
        private String url;
    }
}
