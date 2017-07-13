package com.kuaizhan.kzweixin.mq.consumer;

import com.kuaizhan.kzweixin.mq.dto.FanDTO;
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
    public void onMessage(String message) {
        FanDTO dto = JsonUtil.string2Bean(message, FanDTO.class);
        System.out.println("***************************[SubscribeConsumer]*************************");
        fanService.addFanOpenId(dto.getAppId(), dto.getOpenId());
        fanService.refreshFan(dto.getAppId(), dto.getOpenId());
    }
}
