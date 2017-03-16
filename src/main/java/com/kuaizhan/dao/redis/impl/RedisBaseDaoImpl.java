package com.kuaizhan.dao.redis.impl;
import com.kuaizhan.utils.RedisUtil;
import javax.annotation.Resource;


/**
 * redisDao的基类
 * Created by Mr.Jadyn on 2017/1/19.
 */

public abstract class RedisBaseDaoImpl {

    @Resource
    private RedisUtil redisUtil;


    protected boolean exist(String key) {
        return redisUtil.exists(key);
    }


    protected boolean exist(String key, String field) {
        return redisUtil.exists(key, field);
    }


    protected long getTtl(String key) {
        return redisUtil.getTtl(key);
    }


    protected boolean equal(String key, String str) {
        return redisUtil.equal(key, str);
    }


    protected void setData(String key, String value, int expire) {
        redisUtil.setEx(key, expire, value);
    }


    protected void setData(String key, String field, String value, int expire) {
        redisUtil.setHash(key, field, value);
        redisUtil.setTimeout(key, expire);
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
