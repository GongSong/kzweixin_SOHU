package com.kuaizhan.kzweixin.cache.impl;

import com.kuaizhan.kzweixin.cache.MsgCache;
import com.kuaizhan.kzweixin.constant.RedisConstant;
import com.kuaizhan.kzweixin.utils.RedisUtil;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by zixiong on 2017/5/31.
 */
@Repository("msgCache")
public class MsgRedisImpl implements MsgCache {

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

    @Override
    public Long getMsgIdMapper(String appId, long msgId) {
        String key = RedisConstant.KEY_KZ_MSG_ID + appId + "-" + msgId;
        return Long.parseLong(redisUtil.get(key));
    }

    @Override
    public void setMsgIdMapper(String appId, long msgId, long tplMassId) {
        String key = RedisConstant.KEY_KZ_MSG_ID + appId + "-" + msgId;
        redisUtil.setEx(key, 30 * 60, String.valueOf(tplMassId));
    }

}
