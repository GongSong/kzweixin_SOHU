package com.kuaizhan.kzweixin.entity.LinkGroup;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuaizhan.kzweixin.enums.LinkType;
import lombok.Data;

/**
 * 链接组Model
 * Created by zixiong on 2017/07/30.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LinkGroup {

    // 标题
    // 为什么title等要序列化话成post_title的形式？为了和php兼容
    @JsonProperty("post_title")
    private String title;
    // 摘要
    @JsonProperty("post_description")
    private String description;
    // 封面图
    @JsonProperty("post_pic_url")
    private String picUrl;
    // 链接
    @JsonProperty("post_url")
    private String url;

    // 链接标识
    private String linkId;
    // 链接类型
    private LinkType linkType;
    // 链接别名
    private String linkName;


    // php老代码兼容字段
    @JsonProperty("link_res_type")
    private Integer linkResType;
    @JsonProperty("link_res_id")
    private String linkResId;
    @JsonProperty("link_res_name")
    private String linkResName;

}
