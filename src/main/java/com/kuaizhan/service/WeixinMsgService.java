package com.kuaizhan.service;

import com.kuaizhan.exception.deprecated.system.EncryptException;
import com.kuaizhan.exception.common.XMLParseException;

/**
 * 微信消息服务
 * Created by liangjiateng on 2017/3/20.
 */
public interface WeixinMsgService {

    /**
     * 处理微信推送来的消息
     *
     * @return
     */
    String handleWeixinPushMsg(String appId, String signature, String timestamp, String nonce, String postData) throws EncryptException, XMLParseException;
}
