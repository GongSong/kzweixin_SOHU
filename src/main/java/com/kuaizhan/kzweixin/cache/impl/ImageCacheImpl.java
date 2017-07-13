package com.kuaizhan.kzweixin.cache.impl;

import com.kuaizhan.kzweixin.constant.RedisConstant;
import com.kuaizhan.kzweixin.cache.ImageCache;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.EncryptUtil;
import com.kuaizhan.kzweixin.utils.RedisUtil;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by zixiong on 2017/4/6.
 */

@Repository("imageCache")
public class ImageCacheImpl implements ImageCache {

    @Resource
    RedisUtil redisUtil;

    @Override
    public void setImageUrl(String originUrl, String url) {
        String key = RedisConstant.KEY_IMAGE_WEIXIN_RUL + EncryptUtil.md5(originUrl);
        // 先缓存30天
        redisUtil.setEx(key, 30 * 24 * 60 * 60, url);
    }

    @Override
    public String getImageUrl(String originUrl) {
        String key = RedisConstant.KEY_IMAGE_WEIXIN_RUL + EncryptUtil.md5(originUrl);
        return redisUtil.get(key);
    }

    @Override
    public void setImageUploaded(String url) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String dateStr = dtf.format(localDate);

        String key = RedisConstant.KEY_V2_IMAGE_UPLOADED + dateStr;
        redisUtil.addZset(key, url, (double) DateUtil.curSeconds());
        redisUtil.expire(key, 5 * 24 * 60 * 60);
    }
}
