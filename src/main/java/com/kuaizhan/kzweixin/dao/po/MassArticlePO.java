package com.kuaizhan.kzweixin.dao.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by steffanchen on 2017/7/19.
 */
@Data
public class MassArticlePO {

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("picUrl")
    private String picUrl;

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

    @JsonProperty("cover")
    private String cover;
}
