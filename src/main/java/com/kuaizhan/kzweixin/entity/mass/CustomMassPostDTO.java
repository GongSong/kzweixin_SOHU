package com.kuaizhan.kzweixin.entity.mass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by steffanchen on 2017/7/24.
 */
@Data
public class CustomMassPostDTO {

    @JsonProperty("post_id")
    private long postId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("picurl")
    private String picurl;

    @JsonProperty("url")
    private String url;

}
