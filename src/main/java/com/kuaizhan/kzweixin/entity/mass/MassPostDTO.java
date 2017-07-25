package com.kuaizhan.kzweixin.entity.mass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by steffanchen on 2017/7/24.
 */
@Data
public class MassPostDTO {

    @JsonProperty("post_id")
    private long postId;

    @JsonProperty("post_title")
    private String postTitle;

    @JsonProperty("post_description")
    private String postDescription;

    @JsonProperty("post_pic_url")
    private String postPicUrl;

    @JsonProperty("post_url")
    private String postUrl;

    @JsonProperty("post_type")
    private int postType;
}
