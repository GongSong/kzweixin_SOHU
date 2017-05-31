package com.kuaizhan.cache;

/**
 * Created by zixiong on 2017/5/31.
 */
public interface MsgCache {
    /**
     * 获取快站push token
     */
    String getPushToken(String appId, String openId);

    /**
     * 设置push token
     */
    void setPushToken(String appId, String openId, String token);

    /**
     * 删除push token
     */
    void deletePushToken(String appId, String openId);
}
