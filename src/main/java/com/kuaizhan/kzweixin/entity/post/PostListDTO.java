package com.kuaizhan.kzweixin.entity.post;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 菜单中的图文列表数据
 * Created by zixiong on 2017/5/26.
 */

public class PostListDTO {

    public class Post {
        @JsonProperty("post_id")
        private Long postId;
    }
}
