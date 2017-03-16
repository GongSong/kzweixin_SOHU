package com.kuaizhan.service;


import com.kuaizhan.exception.system.*;
import com.kuaizhan.pojo.DTO.AuthorizationInfoDTO;
import com.kuaizhan.pojo.DTO.AuthorizerInfoDTO;

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
     * @return
     */
    boolean checkMsg(String signature, String timestamp, String nonce) throws EncryptException;

    /**
     * 获取微信推送的component_verify_ticket
     *
     * @return
     */
    void getComponentVerifyTicket(String signature, String timestamp, String nonce, String postData) throws DecryptException, XMLParseException, RedisException;

    /**
     * 获取第三方平台component_access_token
     *
     * @return
     */
    String getComponentAccessToken() throws RedisException, JsonParseException;

    /**
     * 获取预授权码
     *
     * @return
     */
    String getPreAuthCode(String componentAccessToken) throws RedisException, JsonParseException;

    /**
     * 使用授权码换取公众号的接口调用凭据和授权信息
     *
     * @param componentAppId
     * @param authCode
     * @return
     */
    AuthorizationInfoDTO getAuthorizationInfo(String componentAccessToken, String componentAppId, String authCode) throws JsonParseException;

    /**
     * 获取（刷新）授权公众号的接口调用凭据（令牌）
     *
     * @param componentAccessToken
     * @param componentAppId
     * @param authorizerAppId
     * @param authorizerRefreshToken
     * @return
     */
    AuthorizationInfoDTO refreshAuthorizationInfo(String componentAccessToken, String componentAppId, String authorizerAppId, String authorizerRefreshToken) throws JsonParseException;

    /**
     * 获取授权方的公众号帐号基本信息
     *
     * @param componentAccessToken
     * @param componentAppId
     * @param authorizerAppId
     * @return
     */
    AuthorizerInfoDTO getAuthorizerInfo(String componentAccessToken, String componentAppId, String authorizerAppId) throws JsonParseException;

}
