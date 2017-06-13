package com.kuaizhan.kzweixin.dao.redis;

import com.kuaizhan.kzweixin.cache.ImageCache;
import com.kuaizhan.kzweixin.constant.RedisConstant;
import com.kuaizhan.kzweixin.utils.RedisUtil;
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
public class ImageCacheTest {
    @Resource
    private ImageCache imageCache;

    @Resource
    private RedisUtil redisUtil;

    @Test
    public void setAndGetImageUrl() throws Exception {
        String originUrl = "http://kz.testurl.com";
        String weixinUrl = "http://weixin.testurl.com";
        imageCache.setImageUrl(originUrl, weixinUrl);

        assertEquals(weixinUrl, imageCache.getImageUrl(originUrl));

        redisUtil.delete(RedisConstant.KEY_IMAGE_WEIXIN_RUL + originUrl);
        assertEquals(imageCache.getImageUrl(originUrl), null);
    }
}