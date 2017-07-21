package com.kuaizhan.kzweixin.service;

import com.kuaizhan.kzweixin.cache.model.AuthLoginInfo;
import com.kuaizhan.kzweixin.enums.AuthorizeScope;

/**
 * 授权登录相关service
 * Created by zixiong on 2017/7/13.
 */
public interface AuthorizeLoginService {

    /**
     * 获取授权登录跳转url
     * @param appId 要授权登录的公众号
     * @param redirectUrl 授权完成后的跳转url
     * @param scope 授权类型
     * @return 授权跳转url
     */
    String getAuthorizeLoginUrl(String appId, String redirectUrl, AuthorizeScope scope);


    /**
     * 获取授权登录完成后的回调地址，携带者授权信息
     * @param appId 授权登录的公众号
     * @param code 微信返回的
     * @param redirectUrl
     * @return
     */
    String getRedirectUrlWithToken(String appId, String code, String redirectUrl);


    /**
     * 根据token获取用户信息
     */
    AuthLoginInfo getAuthLoginInfoByToken(String token);
}
