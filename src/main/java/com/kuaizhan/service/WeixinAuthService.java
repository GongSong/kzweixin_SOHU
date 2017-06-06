package com.kuaizhan.service;


import com.kuaizhan.exception.common.RedisException;
import com.kuaizhan.exception.deprecated.system.*;
import com.kuaizhan.pojo.dto.AuthorizationInfoDTO;
import com.kuaizhan.pojo.dto.AuthorizerInfoDTO;

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
     * 使用授权码换取公众号的接口调用凭据和授权信息
     */
    AuthorizationInfoDTO getAuthorizationInfo(String componentAccessToken, String componentAppId, String authCode) throws JsonParseException;

    /**
     * 获取（刷新）授权公众号的接口调用凭据（令牌）
     */
    AuthorizationInfoDTO refreshAuthorizationInfo(String componentAccessToken, String componentAppId, String authorizerAppId, String authorizerRefreshToken);

    /**
     * 获取授权方的公众号帐号基本信息
     */
    AuthorizerInfoDTO getAuthorizerInfo(String componentAccessToken, String componentAppId, String authorizerAppId) throws JsonParseException;

}
