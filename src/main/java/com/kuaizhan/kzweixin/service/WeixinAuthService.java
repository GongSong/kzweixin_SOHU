package com.kuaizhan.kzweixin.service;


import com.kuaizhan.kzweixin.exception.deprecated.system.*;
import com.kuaizhan.kzweixin.entity.account.AuthorizationInfoDTO;

/**
 * Created by Mr.Jadyn on 2017/1/19.
 */
public interface WeixinAuthService {
    /**
     * 验证消息的确来自微信服务器
     *
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce     随机数
     */
    boolean checkMsg(String signature, String timestamp, String nonce) throws EncryptException;

    /**
     * 获取微信推送的component_verify_ticket
     */
    void getComponentVerifyTicket(String signature, String timestamp, String nonce, String postData);

    /**
     * 获取第三方平台component_access_token
     */
    String getComponentAccessToken();

    /**
     * 获取预授权码
     */
    String getPreAuthCode();

    /**
     * 获取（刷新）授权公众号的接口调用凭据（令牌）
     */
    AuthorizationInfoDTO refreshAuthorizationInfo(String componentAccessToken, String componentAppId, String authorizerAppId, String authorizerRefreshToken);
}
