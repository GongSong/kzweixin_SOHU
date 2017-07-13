package com.kuaizhan.kzweixin.entity.msg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuaizhan.kzweixin.enums.MsgType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 客服消息数据
 * Created by zixiong on 2017/5/31.
 */
@Data
public class CustomMsg {

    // 消息类型
    @NotNull
    private MsgType msgType;

    // 不同类型的消息数据
    private Text text;
    private Image image;
    private MpNews mpNews;
    private News news;

    // 内容的json数据
    private String contentJsonStr;


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
