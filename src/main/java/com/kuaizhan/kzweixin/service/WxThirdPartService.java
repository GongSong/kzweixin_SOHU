package com.kuaizhan.kzweixin.service;


import com.kuaizhan.kzweixin.exception.deprecated.system.EncryptException;

/**
 * 微信第三方平台相关操作service
 * Created by Mr.Jadyn on 2017/1/19.
 */
public interface WxThirdPartService {
    /**
     * 验证消息的确来自微信服务器
     *
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce     随机数
     */
    boolean checkMsg(String signature, String timestamp, String nonce) throws EncryptException;

    /**
     * 接收微信推送，刷新ComponentVerifyTicket
     */
    void refreshComponentVerifyTicket(String signature, String timestamp, String nonce, String postData);

    /**
     * 获取第三方平台component_access_token
     */
    String getComponentAccessToken();
}
