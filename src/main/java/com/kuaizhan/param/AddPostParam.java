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
public class AddPostParam {

    @NotNull
    private Long weixinAppid;

    @Valid
    @NotNull(message = "多图文列表不能为空")
    @Size(min = 1, max = 8, message = "最少1条多图文，最多8条多图文")
    private List<PostParamItem> posts;


    public Long getWeixinAppid() {
        return weixinAppid;
    }

    public void setWeixinAppid(Long weixinAppid) {
        this.weixinAppid = weixinAppid;
    }

    public List<PostParamItem> getPosts() {
        return posts;
    }

    public void setPosts(List<PostParamItem> posts) {
        this.posts = posts;
    }

    public List<PostDO> getPostDOs() {
        List<PostDO> postDOs = new ArrayList<>();
        for (PostParamItem postParamItem : posts) {
            postDOs.add(postParamItem.toPostDO());
        }
        return postDOs;
    }
}
