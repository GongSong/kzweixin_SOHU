package com.kuaizhan.kzweixin.entity.mass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by steffanchen on 2017/7/24.
 * Table: weixin_custom_mass
 * Column: msg_json
 */
public class MsgJson {
    /*
    *  1文字 2微信图文 3图片 4链接组
    * */

    @Data
    public static class Text {
        @JsonProperty("content")
        private String content;
    }

    @Data
    public static class Articles {
        @JsonProperty("articles")
        private List<CustomMassPostDTO> articles;
    }

    @Data
    public static class Image {
        @JsonProperty("pic_id")
        private String picId;

        @JsonProperty("pic_url")
        private String picUrl;

        @JsonProperty("media_id")
        private String mediaId;
    }

    @Data
    public static class Links {

        @JsonProperty("articles")
        private List<CustomMassArticleDTO> articles;
    }
}
