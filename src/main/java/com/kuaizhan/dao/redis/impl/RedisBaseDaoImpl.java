package com.kuaizhan.dao.redis.impl;

import com.kuaizhan.dao.redis.RedisBaseDao;
import com.kuaizhan.utils.JsonUtil;
import com.kuaizhan.utils.RedisUtil;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * redisDao的基类
 * Created by Mr.Jadyn on 2017/1/19.
 */
@Repository("redisBaseDao")
public class RedisBaseDaoImpl<T> implements RedisBaseDao<T> {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public boolean exist(String key) {
        return redisUtil.exists(key);
    }

    @Override
    public boolean exist(String key, String field) {
        return redisUtil.exists(key, field);
    }

    @Override
    public long getTtl(String key) {
        return redisUtil.getTtl(key);
    }

    @Override
    public boolean equal(String key, String str) {
        return redisUtil.equal(key, str);
    }

    @Override
    public void setData(String key, String value, int expire) {
        redisUtil.setEx(key, expire, value);
    }

    @Override
    public void setData(String key, String field, String value, int expire) {
        redisUtil.setHash(key, field, value);
        redisUtil.setTimeout(key, expire);
    }

    @Override
    public String getData(String key) {
        return redisUtil.get(key);
    }

    @Override
    public String getData(String key, String field) {
        return redisUtil.getHash(key, field);
    }

    @Override
    public void deleteData(String key) {
        redisUtil.delete(key);
    }


}
