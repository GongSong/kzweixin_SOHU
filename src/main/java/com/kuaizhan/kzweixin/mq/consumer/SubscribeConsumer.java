package com.kuaizhan.kzweixin.mq.consumer;

import com.kuaizhan.kzweixin.exception.weixin.WxApiUnauthorizedException;
import com.kuaizhan.kzweixin.exception.weixin.WxApiUnauthorizedToKzException;
import com.kuaizhan.kzweixin.mq.dto.FanDTO;
import com.kuaizhan.kzweixin.service.FanService;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Created by fangtianyu on 7/5/17.
 */
public class SubscribeConsumer extends BaseConsumer{

    @Resource
    private FanService fanService;

    private static final Logger logger = LoggerFactory.getLogger(SubscribeConsumer.class);

    @Override
    public void onMessage(String message) {
        FanDTO dto = JsonUtil.string2Bean(message, FanDTO.class);
        fanService.addFanOpenId(dto.getAppId(), dto.getOpenId());
        try {
            fanService.refreshFan(dto.getAppId(), dto.getOpenId());
        } catch (WxApiUnauthorizedException | WxApiUnauthorizedToKzException e) {
            logger.warn("[mq] api unauthorized. appid: {}", dto.getAppId(), e);
        }
    }
}
