package com.kuaizhan.kzweixin.dao.redis.impl;
import com.kuaizhan.kzweixin.utils.RedisUtil;
import javax.annotation.Resource;


/**
 * redisDao的基类
 * Created by Mr.Jadyn on 2017/1/19.
 */

public abstract class RedisBaseDaoImpl {

    @Resource
    private RedisUtil redisUtil;

    protected long getTtl(String key) {
        return redisUtil.getTtl(key);
    }


    protected void setData(String key, String value, int expire) {
        redisUtil.setEx(key, expire, value);
    }


    protected void setData(String key, String hashKey, String value, int expire) {
        redisUtil.setHash(key, hashKey, value);
        redisUtil.expire(key, expire);
    }


    protected String getData(String key) {
        return redisUtil.get(key);
    }


    protected String getData(String key, String field) {
        return redisUtil.getHash(key, field);
    }


    protected void deleteData(String key) {
        redisUtil.delete(key);
    }


}
