package com.kuaizhan.kzweixin.entity.responsejson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 图文类型的回复，在数据库中的存储结构
 * Created by zixiong on 2017/07/31.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseJson implements ResponseJson {

    // mediaId的列表
    private List<String> mediaIds;

    private List<Post> posts;

    /* php兼容字段 */
    @JsonProperty("post_list")
    private List<Post> postList;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Post {

        private Long pageId;
        private String title;
        // 摘要
        private String description;
        // 封面图
        private String picUrl;
        // 链接
        private String url;

        /* php兼容字段 */
        @JsonProperty("post_id")
        private Long oldPageId;
        @JsonProperty("post_title")
        private String oldTitle;
        @JsonProperty("post_description")
        private String oldDescription;
        @JsonProperty("post_pic_url")
        private String oldPicUrl;
        @JsonProperty("post_url")
        private String oldUrl;
    }

    @Override
    public void cleanBeforeInsert() {
        for (Post post: posts) {
            post.setOldPageId(post.getPageId());
            post.setOldTitle(post.getTitle());
            post.setOldDescription(post.getDescription());
            post.setOldPicUrl(post.getPicUrl());
            post.setOldUrl(post.getUrl());
        }
        setPostList(posts);
        setPosts(null);
    }

    @Override
    public void cleanAfterSelect() {
        setPosts(postList);
        setPostList(null);
        for (Post post: posts) {
            // 是老数据, 转移数据
            if (post.getPageId() == null) {
                post.setPageId(post.getOldPageId());
                post.setTitle(post.getOldTitle());
                post.setDescription(post.getOldDescription());
                post.setPicUrl(post.getOldPicUrl());
                post.setUrl(post.getOldUrl());
            }

            // 清空老数据
            post.setOldPageId(null);
            post.setOldTitle(null);
            post.setOldDescription(null);
            post.setOldPicUrl(null);
            post.setOldUrl(null);
        }
    }
}
