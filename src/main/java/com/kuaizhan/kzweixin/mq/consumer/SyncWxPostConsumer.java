package com.kuaizhan.kzweixin.mq.consumer;

import com.kuaizhan.kzweixin.mq.dto.SyncWxPostDTO;
import com.kuaizhan.kzweixin.entity.post.WxPostDTO;
import com.kuaizhan.kzweixin.service.PostService;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;

/**
 * 单条微信图文消息导入
 *
 * Created by lorin on 17-3-29.
 */
public class SyncWxPostConsumer extends BaseConsumer {

    private static final Logger logger = LoggerFactory.getLogger(SyncWxPostConsumer.class);

    @Resource
    private PostService postService;

    @Override
    public void onMessage(String message) {

        SyncWxPostDTO dto = JsonUtil.string2Bean(message, SyncWxPostDTO.class);

        Long weixinAppid = dto.getWeixinAppid();
        Integer updateTime = dto.getUpdateTime();
        String mediaId = dto.getMediaId();
        Boolean isNew = dto.getIsNew();
        List<WxPostDTO> wxPostDTOS = dto.getWxPostDTOS();

        // 新增
        if (isNew) {
            if (!postService.existPost(weixinAppid, mediaId)) {
                postService.importWeixinPost(weixinAppid, mediaId, updateTime, wxPostDTOS);
            }
        // 更新
        } else {
            postService.updateWeixinPost(weixinAppid, mediaId, updateTime, wxPostDTOS);
        }
    }
}
