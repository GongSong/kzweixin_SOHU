package com.kuaizhan.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuaizhan.constant.MqConstant;
import com.kuaizhan.exception.BaseException;
import com.kuaizhan.pojo.DTO.WxPostListDTO;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.PostService;
import com.kuaizhan.service.WeixinPostService;
import com.kuaizhan.utils.JsonUtil;
import com.kuaizhan.utils.LogUtil;
import com.kuaizhan.utils.MqUtil;
import org.apache.log4j.Logger;

import java.util.*;

import javax.annotation.Resource;

/**
 * 图文消息导入
 *
 * Created by lorin on 17-3-29.
 */
public class WeixinPostListConsumer extends BaseMqConsumer {

    @Resource
    PostService postService;

    private static final Logger logger = Logger.getLogger(WeixinPostListConsumer.class);

    @Override
    public void onMessage(Map msgMap) throws Exception {
        logger.info("[mq:同步微信图文], map: "+ msgMap);

        long weixinAppid = (long) msgMap.get("weixinAppid");
        long userId = (long) msgMap.get("uid");

        // 获取不存在的微信图文
        postService.calSyncWeixinPosts(weixinAppid, userId);
    }
}
