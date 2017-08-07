package com.kuaizhan.kzweixin.entity.responsejson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuaizhan.kzweixin.enums.LinkType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 链接组类型的结构在数据库中的存储结构
 * Created by zixiong on 2017/07/31.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkGroupResponseJson implements ResponseJson {

    private List<LinkGroup> linkGroups;

    /* php兼容字段 */
    @JsonProperty("url_list")
    private List<LinkGroup> urlList;

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

        // 链接标识
        private List<String> linkIds;
        // 链接类型
        @NotNull
        private LinkType linkType;


        /* php老代码兼容字段 */
        @JsonProperty("post_title")
        private String oldTitle;
        @JsonProperty("post_description")
        private String oldDescription;
        @JsonProperty("post_pic_url")
        private String oldPicUrl;
        @JsonProperty("post_url")
        private String oldUrl;
        @JsonProperty("link_res_type")
        private Integer oldLinkType;
        @JsonProperty("link_res_id")
        private String OldLinkId;
        @JsonProperty("link_res_name")
        private String oldLinkName;
    }

    @Override
    public void cleanBeforeInsert() {
        for (LinkGroup linkGroup: linkGroups) {
            linkGroup.setOldTitle(linkGroup.getTitle());
            linkGroup.setOldDescription(linkGroup.getDescription());
            linkGroup.setOldPicUrl(linkGroup.getPicUrl());
            linkGroup.setOldUrl(linkGroup.getUrl());
            // 兼容的linkType全部设置为URL
            linkGroup.setOldLinkType(1);
            linkGroup.setOldLinkName(linkGroup.getUrl()); // 老php的linkName等于url
        }
        // 把属性转移到urlList
        setUrlList(linkGroups);
        setLinkGroups(null);
    }

    @Override
    public void cleanAfterSelect() {
        setLinkGroups(urlList);
        setUrlList(null);
        for (LinkGroup linkGroup: linkGroups) {
            // 如果是老数据，转移数据到新字段
            if (linkGroup.getLinkType() == null) {
                linkGroup.setTitle(linkGroup.getOldTitle());
                linkGroup.setDescription(linkGroup.getOldDescription());
                linkGroup.setPicUrl(linkGroup.getOldPicUrl());
                linkGroup.setUrl(linkGroup.getOldUrl());
                // 老数据转新数据，全部改为url类型
                linkGroup.setLinkType(LinkType.URL);
            }

            // 清空老数据字段，向前端屏蔽
            linkGroup.setOldTitle(null);
            linkGroup.setOldDescription(null);
            linkGroup.setOldPicUrl(null);
            linkGroup.setOldUrl(null);
            linkGroup.setOldLinkId(null);
            linkGroup.setOldLinkType(null);
            linkGroup.setOldLinkName(null); // 老php的linkName等于url
        }
    }
}
