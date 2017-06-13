package com.kuaizhan.kzweixin.controller.param;

import com.kuaizhan.kzweixin.dao.po.PostPO;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zixiong on 2017/4/12.
 */
@Data
public class PostsParam {

    @NotNull
    private Long weixinAppid;

    @Valid
    @NotNull(message = "多图文列表不能为空")
    @Size(min = 1, max = 8, message = "最少1条多图文，最多8条多图文")
    private List<PostsParamItem> posts;

    public List<PostPO> getPostDOs() {
        List<PostPO> postPOS = new ArrayList<>();
        for (PostsParamItem postsParamItem : posts) {
            postPOS.add(postsParamItem.toPostDO());
        }
        return postPOS;
    }
}
