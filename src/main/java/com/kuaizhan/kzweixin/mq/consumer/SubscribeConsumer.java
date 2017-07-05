package com.kuaizhan.kzweixin.mq.consumer;

import com.kuaizhan.kzweixin.mq.dto.SubscribeDTO;
import com.kuaizhan.kzweixin.service.FanService;
import com.kuaizhan.kzweixin.utils.JsonUtil;

import javax.annotation.Resource;

/**
 * Created by fangtianyu on 7/5/17.
 */
public class SubscribeConsumer extends BaseConsumer{

    @Resource
    private FanService fanService;

    @Override
    void onMessage(String message) {
        SubscribeDTO dto = JsonUtil.string2Bean(message, SubscribeDTO.class);
        fanService.addFanOpenId(dto.getAppId(), dto.getOpenId());
        fanService.addFan(dto.getAppId(), dto.getOpenId());
    }
}
