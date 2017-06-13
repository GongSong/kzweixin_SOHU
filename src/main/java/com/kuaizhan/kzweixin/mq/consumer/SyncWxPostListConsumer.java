package com.kuaizhan.kzweixin.mq.consumer;

import com.kuaizhan.kzweixin.mq.dto.SyncWxPostListDTO;
import com.kuaizhan.kzweixin.service.PostService;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * 图文消息导入
 *
 * Created by lorin on 17-3-29.
 */
public class SyncWxPostListConsumer extends BaseConsumer {

    @Resource
    private PostService postService;

    private static final Logger logger = LoggerFactory.getLogger(SyncWxPostListConsumer.class);

    @Override
    public void onMessage(String message) {
        logger.info("[mq:同步微信图文], map:{}", message);
        SyncWxPostListDTO dto = JsonUtil.string2Bean(message, SyncWxPostListDTO.class);

        // 获取不存在的微信图文
        postService.calSyncWeixinPosts(dto.getWeixinAppid(), dto.getUserId());
    }
}
