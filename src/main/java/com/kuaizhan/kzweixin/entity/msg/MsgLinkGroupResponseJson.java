package com.kuaizhan.kzweixin.entity.msg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuaizhan.kzweixin.entity.responsejson.ResponseJson;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by zixiong on 2017/08/03.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MsgLinkGroupResponseJson implements ResponseJson {

    private List<LinkGroup> linkGroups;

    /* php兼容字段 */
    @JsonProperty("articles")
    private List<LinkGroup> articles;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LinkGroup {

        @NotNull
        private String title;
        // 摘要
        private String description;
        // 封面图
        @NotNull
        private String picUrl;
        // 链接
        @NotNull
        private String url;

        /* php兼容字段 */
        @JsonProperty("picurl")
        private String oldPicUrl;
    }

    @Override
    public void cleanBeforeInsert() {
        for (LinkGroup linkGroup: linkGroups) {
            linkGroup.setOldPicUrl(linkGroup.getPicUrl());
        }
        setArticles(linkGroups);
        setLinkGroups(null);
    }

    @Override
    public void cleanAfterSelect() {
        setLinkGroups(articles);
        setArticles(null);
        for (LinkGroup linkGroup: linkGroups) {
            if (linkGroup.getPicUrl() == null) {
                linkGroup.setPicUrl(linkGroup.getOldPicUrl());
            }
            linkGroup.setOldPicUrl(null);
        }
    }
}
