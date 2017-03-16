package com.kuaizhan.dao.redis;

/**
 * Created by Mr.Jadyn on 2017/1/19.
 */
public interface RedisAuthDao extends RedisBaseDao {

    String getComponentVerifyTicket();

    void setComponentVerifyTicket(String ticket);

    String getComponentAccessToken();

    void setComponentAccessToken(String componentAccessToken);

    boolean existComponentAccessToken();

    boolean equalComponentAccessToken(String componentAccessToken);

    String getPreAuthCode();

    void setPreAuthCode(String preAuthCode);

    boolean existPreAuthCode();

    boolean equalPreAuthCode(String preAuthCode);


//
//    String getAccessToken(Long siteId);
//
//    void setAccessToken(Long siteId, String accessToken);
//
//    boolean existAccessToken(Long siteId);
//
//    boolean equalAccessToken(Long siteId, String accessToken);
//
//    void deleteAccessToken(Long siteId);
//
//    String getAppId(Long siteId);
//
//    void setAppId(Long siteId, String appId);
//
//    boolean existAppId(Long siteId);
//
//    void deleteAppId(Long siteId);
//
//    boolean equalAppId(Long siteId, String appId);

}
