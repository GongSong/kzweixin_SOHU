package com.kuaizhan.kzweixin.dao.redis.impl;

import com.kuaizhan.kzweixin.constant.RedisConstant;
import com.kuaizhan.kzweixin.dao.redis.RedisImageDao;
import com.kuaizhan.kzweixin.utils.RedisUtil;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by zixiong on 2017/4/6.
 */

@Repository("redisImageDao")
public class RedisImageDaoImpl implements RedisImageDao{

    @Resource
    RedisUtil redisUtil;

    @Override
    public void setImageUrl(String originUrl, String url) {
        String key = RedisConstant.KEY_IMAGE_WEIXIN_RUL + originUrl;
        // 先缓存30天
        redisUtil.setEx(key, 30 * 24 * 60 * 60, url);
    }

    @Override
    public String getImageUrl(String originUrl) {
        String key = RedisConstant.KEY_IMAGE_WEIXIN_RUL + originUrl;
        return redisUtil.get(key);
    }
}
