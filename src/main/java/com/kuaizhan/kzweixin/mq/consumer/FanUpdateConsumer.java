package com.kuaizhan.kzweixin.mq.consumer;

import com.kuaizhan.kzweixin.dao.po.auto.FanPO;
import com.kuaizhan.kzweixin.mq.dto.FanDTO;
import com.kuaizhan.kzweixin.service.FanService;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;

import javax.annotation.Resource;

/**
 * Created by fangtianyu on 7/12/17.
 */
public class FanUpdateConsumer extends BaseConsumer{

    @Resource
    private FanService fanService;

    @Override
    public void onMessage(String message) {
        FanDTO fanDTO = JsonUtil.string2Bean(message, FanDTO.class);
        String appId = fanDTO.getAppId();
        String openId = fanDTO.getOpenId();
        fanService.addFanOpenId(appId, openId);

        FanPO fanPO = fanService.getFanByOpenId(appId, openId);
        System.out.println("------------------------[FanUpdateConsumer]-------------------------");
        System.out.println("[FanUpdateConsumer]fanPO fanId:" + fanPO.getFanId() + " fanNickname:" + fanPO.getNickName());
        System.out.println("DateUtil.curSeconds():" + DateUtil.curSeconds());
        System.out.println("fanPO.getUpdateTime():" + fanPO.getUpdateTime());
        System.out.println(DateUtil.curSeconds() - fanPO.getUpdateTime());
        if (fanPO == null || DateUtil.curSeconds() - fanPO.getUpdateTime() > 3600) {
            System.out.println("##############################in the if clause#################################");
            fanService.refreshFan(appId, openId);
        }
    }
}
