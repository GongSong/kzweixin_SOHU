package com.kuaizhan.kzweixin.cache.impl;

import com.kuaizhan.kzweixin.cache.ActionCache;
import com.kuaizhan.kzweixin.constant.RedisConstant;
import com.kuaizhan.kzweixin.utils.RedisUtil;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by zixiong on 2017/07/27.
 */
@Repository
public class ActionCacheImpl implements ActionCache {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public String setOpenId(String openId, int expireIn) {
        String token = UUID.randomUUID().toString();
        // 缓存一天
        redisUtil.setEx(RedisConstant.KEY_ACTION_OPEN_ID + token, expireIn, openId);
        return token;
    }

    @Override
    public String getOpenId(String token) {
        return redisUtil.get(RedisConstant.KEY_ACTION_OPEN_ID + token);
    }

    @Override
    public void deleteOpenId(String token) {
        redisUtil.delete(RedisConstant.KEY_ACTION_OPEN_ID + token);
    }
}
