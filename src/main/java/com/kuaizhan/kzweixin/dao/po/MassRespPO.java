package com.kuaizhan.kzweixin.dao.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by Chen on 17/7/11.
 */
@Data
public class MassRespPO {

    @JsonProperty("post_id")
    private int postId;

    @JsonProperty("post_type")
    private int postType;

    @JsonProperty("title")
    private String title;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("cover")
    private String cover;

}
