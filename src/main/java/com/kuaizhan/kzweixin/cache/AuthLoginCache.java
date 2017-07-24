package com.kuaizhan.kzweixin.cache;


import com.kuaizhan.kzweixin.cache.model.AuthLoginInfo;

public interface AuthLoginCache {
    /**
     * 设置授权登录缓存
     * @return token
     */
    String setAuthLoginInfo(AuthLoginInfo authLoginInfo);

    /**
     * 获取授权登录缓存
     */
    AuthLoginInfo getAuthLoginInfo(String token);
}
