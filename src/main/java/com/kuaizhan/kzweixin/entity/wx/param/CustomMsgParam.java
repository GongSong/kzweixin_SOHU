package com.kuaizhan.kzweixin.entity.wx.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 发送客服消息的Param对象
 * 接口文档： https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140547
 * Created by zixiong on 2017/08/04.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomMsgParam {
    // openId
    private String touser;
    @JsonProperty("msgtype")
    private String msgType;

    // 文本消息
    private Text text;

    // 图片消息
    private Image image;

    // 链接组消息
    private News news;

    @Data
    @AllArgsConstructor
    public static class Text {
        private String content;
    }

    @Data
    @AllArgsConstructor
    public static class Image {
        @JsonProperty("media_id")
        private String mediaId;
    }

    @Data
    @AllArgsConstructor
    public static class News {
        private List<Article> articles;
    }

    @Data
    public static class Article {
        private String title;
        private String description;
        @JsonProperty("picurl")
        private String picUrl;
        private String url;
    }
}
