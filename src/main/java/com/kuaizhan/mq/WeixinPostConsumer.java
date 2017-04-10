package com.kuaizhan.mq;

import com.kuaizhan.exception.BaseException;
import com.kuaizhan.pojo.DTO.PostDTO;
import com.kuaizhan.service.PostService;
import com.kuaizhan.utils.JsonUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * 单条微信图文消息导入
 *
 * Created by lorin on 17-3-29.
 */
public class WeixinPostConsumer extends BaseMqConsumer {

    @Resource
    PostService postService;

    @Override
    protected void onMessage(Map msgMap) throws Exception {
        long userId = (long) msgMap.get("userId");
        String postItemJson = (String) msgMap.get("postItem");
        PostDTO.PostItem postItem = JsonUtil.string2Bean(postItemJson, PostDTO.PostItem.class);
        postService.importWeixinPost(postItem, userId);
    }
}
