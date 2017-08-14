package com.kuaizhan.kzweixin.cache;

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

    /**
     * 获取msgId-massId键值对
     * */
    Long getMsgIdMapper(String appId, long msgId);

    /**
     * 增加msgId-massId键值对
     * */
    void setMsgIdMapper(String appId, long msgId, long massId);

}
