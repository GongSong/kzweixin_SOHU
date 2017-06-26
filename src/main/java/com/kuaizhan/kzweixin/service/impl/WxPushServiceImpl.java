package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.manager.KzManager;
import com.kuaizhan.kzweixin.service.CommonService;
import com.kuaizhan.kzweixin.service.WxPushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zixiong on 2017/6/23.
 */
@Service
public class WxPushServiceImpl implements WxPushService {

    @Resource
    private CommonService commonService;

    private static final Logger logger = LoggerFactory.getLogger(WxPushServiceImpl.class);

    @Override
    public String handleEventPush(String appId, String signature, String timestamp, String nonce, String xmlStr) {

        commonService.kzStat("weixin", "a000");
        commonService.kzStat("weixin", "a000" + appId);

        String result = KzManager.kzResponseMsg(appId, timestamp, nonce, xmlStr);
        logger.info("php result:", result);
        return result;
    }
}
