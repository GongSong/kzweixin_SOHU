package com.kuaizhan.dao.redis;

/**
 * Created by zixiong on 2017/4/6.
 */
public interface RedisImageDao {

    /**
     * 把上传过的图片url缓存起来
     * @param originUrl 原来的url
     * @param url 在微信的url
     */
    void setImageUrl(String originUrl, String url);

    /**
     * 获取缓存的上传过的图片url
     */
    String getImageUrl(String originUrl);

    /**
     * 临时缓存使用v2版接口上传的image url
     */
    void setImageUploaded(String url);
}
