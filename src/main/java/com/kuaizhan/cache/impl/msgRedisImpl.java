package com.kuaizhan.cache.impl;

import com.kuaizhan.cache.MsgCache;
import com.kuaizhan.constant.RedisConstant;
import com.kuaizhan.utils.RedisUtil;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by zixiong on 2017/5/31.
 */
@Repository("msgCache")
public class msgRedisImpl implements MsgCache {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public String getPushToken(String appId, String openId) {
        String key = RedisConstant.KEY_KZ_PUSH_TOKEN + appId + "-" + openId;
        return redisUtil.get(key);
    }

    @Override
    public void setPushToken(String appId, String openId, String cache) {
        String key = RedisConstant.KEY_KZ_PUSH_TOKEN + appId + "-" + openId;
        redisUtil.setEx(key, 30 * 60, cache);
    }

    @Override
    public void deletePushToken(String appId, String openId) {
        String key = RedisConstant.KEY_KZ_PUSH_TOKEN + appId + "-" + openId;
        redisUtil.delete(key);
    }
}
