package com.kuaizhan.kzweixin.entity.mass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by steffanchen on 2017/7/24.
 */
@Data
public class CustomMassArticleDTO {

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("picurl")
    private String picurl;

    @JsonProperty("cover")
    private String cover;

    @JsonProperty("url")
    private String url;

    @JsonProperty("link")
    private String link;

    @JsonProperty("link_res_id")
    private long linkResId;

    @JsonProperty("link_res_name")
    private String linkResName;

    @JsonProperty("link_res_type")
    private int linkResType;

    @JsonProperty("summary")
    private String summary;
}
