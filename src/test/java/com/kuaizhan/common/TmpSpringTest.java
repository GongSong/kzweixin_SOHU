package com.kuaizhan.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by zixiong on 2017/4/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class TmpSpringTest {

    @Resource
    StringRedisTemplate template;

    @Resource
    StringRedisTemplate template0;

    @Test
    public void testRedisTemplate() throws Exception {
//        System.out.println("---->" + template.opsForValue().get("a"));

//        template0.opsForValue().set("a", "1");
//        template0.expire("a", 1, TimeUnit.SECONDS);
//        System.out.println("---->" + template0.opsForValue().get("a"));
    }
}
