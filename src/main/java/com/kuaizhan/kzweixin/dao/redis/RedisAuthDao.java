package com.kuaizhan.kzweixin.dao.redis;

/**
 * Created by Mr.Jadyn on 2017/1/19.
 */
public interface RedisAuthDao {

    String getComponentVerifyTicket();

    void setComponentVerifyTicket(String ticket);

    String getComponentAccessToken();

    void setComponentAccessToken(String componentAccessToken);
}
