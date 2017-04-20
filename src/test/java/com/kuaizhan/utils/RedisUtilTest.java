package com.kuaizhan.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by zixiong on 2017/4/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class RedisUtilTest {

    @Resource
    RedisUtil redisUtil;

    private static final String redisTestFrefix = "test_kzweixin_test:";
    private static final String key = redisTestFrefix + "test";

    @Test
    public void getAndSetAndDelete() throws Exception {
        String value = "中文";
        redisUtil.set(key, value);
        assertEquals(value, redisUtil.get(key));

        redisUtil.delete(key);
        assertEquals(null, redisUtil.get(key));
    }

    @Test
    public void setExAndGetTtl() throws Exception {
        redisUtil.setEx(key,10,  "whatevert");
        System.out.println("---->" + "sleeping");
        Thread.sleep(3000);
        System.out.println("---->" + "awake");
        System.out.println("---->" + redisUtil.getTtl(key));
    }

    @Test
    public void setHashAndGetHash() throws Exception {
        String hashKey = "hashKey";
        String hashValue = "我是个中文啊";
        redisUtil.setHash(key, hashKey, hashValue);

        System.out.println("---->" + redisUtil.getHash(key, hashKey));
        assertEquals(redisUtil.getHash(key, hashKey), hashValue);

        redisUtil.delete(key);
        assertEquals(redisUtil.getHash(key, hashKey), null);
    }
}