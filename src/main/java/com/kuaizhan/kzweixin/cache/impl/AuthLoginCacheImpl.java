package com.kuaizhan.kzweixin.cache.impl;

import com.kuaizhan.kzweixin.cache.AuthLoginCache;
import com.kuaizhan.kzweixin.cache.model.AuthLoginInfo;
import com.kuaizhan.kzweixin.constant.RedisConstant;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import com.kuaizhan.kzweixin.utils.RedisUtil;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.UUID;

@Repository
public class AuthLoginCacheImpl implements AuthLoginCache {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public String setAuthLoginInfo(AuthLoginInfo authLoginInfo) {
        String token = UUID.randomUUID().toString();
        redisUtil.setEx(RedisConstant.KEY_AUTH_LOGIN_INFO + token, 2 * 60 * 60, JsonUtil.bean2String(authLoginInfo));
        return token;
    }

    @Override
    public AuthLoginInfo getAuthLoginInfo(String token) {
        String cacheStr = redisUtil.get(RedisConstant.KEY_AUTH_LOGIN_INFO + token);
        if (cacheStr == null) {
            return null;
        }
        return JsonUtil.string2Bean(cacheStr, AuthLoginInfo.class);
    }
}
