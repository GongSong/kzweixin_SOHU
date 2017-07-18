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
        // 存在垃圾数据，在这里做次补偿
        fanService.addFanOpenId(appId, openId);

        FanPO fanPO = fanService.getFanByOpenId(appId, openId);
        // 一个小时内更新过的用户， 不再更新
        if (fanPO == null || DateUtil.curSeconds() - fanPO.getUpdateTime() > 3600) {
            fanService.refreshFan(appId, openId);
        }
    }
}
