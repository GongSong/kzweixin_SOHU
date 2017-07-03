package com.kuaizhan.kzweixin.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


/**
 * redis操作工具类
 * Created by Mr.Jadyn on 2016/12/27.
 */
@Component("redisUtil")
public final class RedisUtil {

    @Resource
    private StringRedisTemplate redisTemplate;

    /**
     * 设置String数据类型
     *
     * @param key   key
     * @param value 值
     * @return
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置值以及设置缓存时间
     *
     * @param key    key
     * @param value  值
     * @param expire 剩余时间
     */
    public void setEx(String key, int expire, String value) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * 获取String数据类型
     *
     * @param key key
     * @return
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    /**
     * 获取数据的缓存剩余时间
     *
     * @param key key
     * @return
     */
    public long getTtl(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 删除数据
     *
     * @param key key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 设置缓存时间
     *
     * @param key    key
     * @param expire 时间(秒)
     */
    public void expire(String key, int expire) {
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * 为哈希表中的字段赋值
     * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作
     * 如果字段已经存在于哈希表中，旧值将被覆盖
     *
     * @param key   key
     * @param hashKey 字段
     * @param value 值
     */
    public void setHash(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 返回哈希表中指定字段的值
     *
     * @param key   key
     * @param hashKey 字段
     * @return
     */
    public String getHash(String key, String hashKey) {
        return (String) redisTemplate.opsForHash().get(key, hashKey);
    }
}
