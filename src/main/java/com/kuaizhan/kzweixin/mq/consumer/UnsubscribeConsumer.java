package com.kuaizhan.kzweixin.mq.consumer;

import com.kuaizhan.kzweixin.mq.dto.FanDTO;
import com.kuaizhan.kzweixin.service.FanService;
import com.kuaizhan.kzweixin.utils.JsonUtil;

import javax.annotation.Resource;

/**
 * Created by fangtianyu on 7/12/17.
 */
public class UnsubscribeConsumer extends BaseConsumer{

    @Resource
    private FanService fanService;

    @Override
    public void onMessage(String message) {
        FanDTO fanDTO = JsonUtil.string2Bean(message, FanDTO.class);
        fanService.delFanOpenId(fanDTO.getAppId(), fanDTO.getOpenId());
        fanService.delFan(fanDTO.getAppId(), fanDTO.getOpenId());
    }
}

