package com.kuaizhan.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 链接组Bean
 * Created by zixiong on 2017/5/27.
 */
@Data
public class LinkList {
    @NotNull(message = "标题不能为空")
    private String title;

    private String summary;

    private String link;

    private String cover;

    @JsonProperty("link_res_id")
    private String linkResId;

    @JsonProperty("link_res_type")
    private int linkResType;

    @JsonProperty("link_res_name")
    private String linkResName;
}
