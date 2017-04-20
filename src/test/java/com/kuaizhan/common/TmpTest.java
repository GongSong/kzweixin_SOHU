package com.kuaizhan.common;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.net.URI;

/**
 * 临时测试入口, 不用加载spring的测试
 * Created by zixiong on 2017/4/12.
 */
public class TmpTest {

    @Test
    public void test() throws Exception {
//        redis.host=10.23.72.97
//        redis.port=6379
//        redis.password=crs-2krquz24:kuaizhanisgood!
//        Jedis jedis = new Jedis("10.23.72.97", 6379);
        Jedis jedis = new Jedis("10.23.72.97", 6379);
        jedis.auth("crs-2krquz24:kuaizhanisgood!");
        System.out.println("---->" + jedis.get("a"));
    }
}
