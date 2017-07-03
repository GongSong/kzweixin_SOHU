package com.kuaizhan.dao.redis;

import com.kuaizhan.constant.RedisConstant;
import com.kuaizhan.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by zixiong on 2017/4/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class RedisImageDaoTest {
    @Resource
    RedisImageDao redisImageDao;

    @Resource
    RedisUtil redisUtil;

    @Test
    public void setAndGetImageUrl() throws Exception {
        String originUrl = "http://kz.testurl.com";
        String weixinUrl = "http://weixin.testurl.com";
        redisImageDao.setImageUrl(originUrl, weixinUrl);

        assertEquals(weixinUrl, redisImageDao.getImageUrl(originUrl));

        redisUtil.delete(RedisConstant.KEY_IMAGE_WEIXIN_RUL + originUrl);
        assertEquals(redisImageDao.getImageUrl(originUrl), null);
    }

    @Test
    public void setImageUploaded() throws Exception {
        redisImageDao.setImageUploaded("www.baidu.com");
    }
}