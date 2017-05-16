package com.kuaizhan.mq;

import com.kuaizhan.pojo.dto.WxPostDTO;
import com.kuaizhan.service.PostService;
import com.kuaizhan.utils.JsonUtil;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 单条微信图文消息导入
 *
 * Created by lorin on 17-3-29.
 */
public class WeixinPostConsumer extends BaseMqConsumer {

    private static final Logger logger = Logger.getLogger(WeixinPostConsumer.class);

    @Resource
    PostService postService;

    @Override
    protected void onMessage(Map msgMap) throws Exception {

        long userId = (long) msgMap.get("userId");
        long weixinAppid = (long) msgMap.get("weixinAppid");
        long updateTime = (long) (int) msgMap.get("updateTime");
        String mediaId = (String) msgMap.get("mediaId");
        Boolean isNew = (boolean) msgMap.get("isNew");

        List<WxPostDTO> wxPostDTOS = JsonUtil.string2List((String) msgMap.get("wxPostDTOs"), WxPostDTO.class);

        // 新增
        if (isNew) {
            if (!postService.exist(weixinAppid, mediaId)) {
                logger.info("[mq:从微信导入单条图文], weixinAppid: " + weixinAppid + " mediaId: " + mediaId);
                postService.importWeixinPost(weixinAppid, mediaId, updateTime, userId, wxPostDTOS);
            } else {
                logger.info("[mq:图文已存在], weixinAppid: " + weixinAppid + " mediaId: " + mediaId);
            }
        // 更新
        } else {
            postService.updateWeixinPost(weixinAppid, mediaId, updateTime, userId, wxPostDTOS);
        }
    }
}
