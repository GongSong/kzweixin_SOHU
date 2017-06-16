package com.kuaizhan.kzweixin.cache;

/**
 * Created by Mr.Jadyn on 2017/1/19.
 */
public interface AuthCache {

    /**
     * ticket缓存
     */
    String getComponentVerifyTicket();

    void setComponentVerifyTicket(String ticket);

    /**
     * ComponentAccessToken缓存
     */
    String getComponentAccessToken();

    void setComponentAccessToken(String componentAccessToken);
}
