package com.kuaizhan.kzweixin.service;

import com.kuaizhan.kzweixin.entity.WxData;
import com.kuaizhan.kzweixin.enums.ActionType;

/**
 * 处理微信事件推送
 * Created by zixiong on 2017/6/23.
 */
public interface WxPushService {

    /**
     * 微信服务器事件推送处理
     * @param xmlStr xml数据
     * @return 返回字符串
     */
    String handleEventPush(String appId, String signature, String timestamp, String nonce, String xmlStr);

    String handleActions(long weixinAppid, WxData wxData, ActionType actionType);
}
