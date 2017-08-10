package com.kuaizhan.kzweixin.entity.msg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen on 17/7/14.
 */
public class MassMsg {

    @Data
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Filter {
        @NotNull(message = "is_to_all can not be null")
        @JsonProperty("is_to_all")
        boolean isToAll;

        @NotNull(message = "tag_id can not be null")
        @JsonProperty("tag_id")
        int tagId;
    }


    /**
     * 图文消息
     */
    @Data
    @AllArgsConstructor
    @JsonIgnoreProperties(value = "posts")
    public static class MpNews {
        @JsonProperty("media_id")
        private String mediaId;

        @Size(min = 1, max = 8)
        private List<Long> posts = new ArrayList<>();
    }

    /**
     * 文本
     */
    @Data
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Text {
        String content;
    }

    /**
     * 图片
     */
    @Data
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Image {
        @JsonProperty("media_id")
        private String mediaId;
    }
}
