package com.kuaizhan.kzweixin.entity.mass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by steffanchen on 2017/7/24.
 * Table:weixin_mass
 * Column: response_json
 */
public class ResponseJson {

    /*
     * 1 文章列表，2 页面，3 文字，4 图片
     */

    @Data
    public static class Articles {

        @JsonProperty("post_list")
        private List<MassPostDTO> postList;

        @JsonProperty("media_id")
        private String mediaId;
    }

    @Data
    public static class Page {

    }

    @Data
    public static class Text {
        @JsonProperty("content")
        private String content;
    }

    @Data
    public static class Image {

        @JsonProperty("pic_id")
        public String picId;

        @JsonProperty("pic_url")
        public String picUrl;

        @JsonProperty("media_id")
        public String mediaId;
    }

}
