package com.kuaizhan.kzweixin.service;


/**
 * 微信第三方平台相关操作service
 * Created by Mr.Jadyn on 2017/1/19.
 */
public interface WxThirdPartService {

    /**
     * 解密微信回调的消息体
     * @param msg 解密前文本
     * @return 机密后文本
     */
    String decryptMsg(String signature, String timestamp, String nonce, String msg);

    /**
     * 接收微信推送，刷新ComponentVerifyTicket
     * @param xmlStr 微信推送的xml
     */
    void refreshComponentVerifyTicket(String xmlStr);

    /**
     * 获取第三方平台component_access_token
     */
    String getComponentAccessToken();
}
