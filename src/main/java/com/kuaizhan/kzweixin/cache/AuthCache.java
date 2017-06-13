package com.kuaizhan.kzweixin.cache;

/**
 * Created by Mr.Jadyn on 2017/1/19.
 */
public interface AuthCache {

    String getComponentVerifyTicket();

    void setComponentVerifyTicket(String ticket);

    String getComponentAccessToken();

    void setComponentAccessToken(String componentAccessToken);
}
