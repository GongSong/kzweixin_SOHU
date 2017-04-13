package com.kuaizhan.param;

import com.kuaizhan.pojo.DO.PostDO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zixiong on 2017/4/12.
 */
public class PostsParam {

    @NotNull
    private Long weixinAppid;

    @Valid
    @NotNull(message = "多图文列表不能为空")
    @Size(min = 1, max = 8, message = "最少1条多图文，最多8条多图文")
    private List<PostsParamItem> posts;


    public Long getWeixinAppid() {
        return weixinAppid;
    }

    public void setWeixinAppid(Long weixinAppid) {
        this.weixinAppid = weixinAppid;
    }

    public List<PostsParamItem> getPosts() {
        return posts;
    }

    public void setPosts(List<PostsParamItem> posts) {
        this.posts = posts;
    }

    public List<PostDO> getPostDOs() {
        List<PostDO> postDOs = new ArrayList<>();
        for (PostsParamItem postsParamItem : posts) {
            postDOs.add(postsParamItem.toPostDO());
        }
        return postDOs;
    }
}
