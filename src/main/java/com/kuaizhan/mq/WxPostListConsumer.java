package com.kuaizhan.mq;

import com.kuaizhan.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import javax.annotation.Resource;

/**
 * 图文消息导入
 *
 * Created by lorin on 17-3-29.
 */
public class WxPostListConsumer extends BaseMqConsumer {

    @Resource
    PostService postService;

    private static final Logger logger = LoggerFactory.getLogger(WxPostListConsumer.class);

    @Override
    public void onMessage(Map msgMap) throws Exception {
        logger.info("[mq:同步微信图文], map:{}", msgMap);

        long weixinAppid = (long) msgMap.get("weixinAppid");
        long userId = (long) msgMap.get("uid");

        // 获取不存在的微信图文
        postService.calSyncWeixinPosts(weixinAppid, userId);
    }
}
