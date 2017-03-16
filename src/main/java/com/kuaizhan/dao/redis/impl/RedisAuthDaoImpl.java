package com.kuaizhan.dao.redis.impl;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.redis.RedisAuthDao;
import org.springframework.stereotype.Repository;


/**
 * Created by Mr.Jadyn on 2017/1/19.
 */
@Repository("redisAuthDao")
public class RedisAuthDaoImpl extends RedisBaseDaoImpl implements RedisAuthDao {

    @Override
    public String getComponentVerifyTicket() {
        return getData(ApplicationConfig.KEY_WEIXIN_COMPONENT_VERIFY_TICKET);
    }

    @Override
    public void setComponentVerifyTicket(String ticket) {
        setData(ApplicationConfig.KEY_WEIXIN_COMPONENT_VERIFY_TICKET, ticket, 11 * 60);
    }

    @Override
    public String getComponentAccessToken() {
        return getData(ApplicationConfig.KEY_WEIXIN_COMPONENT_ACCESS_TOKEN);
    }

    @Override
    public void setComponentAccessToken(String componentAccessToken) {
        setData(ApplicationConfig.KEY_WEIXIN_COMPONENT_ACCESS_TOKEN, componentAccessToken, 110 * 60);
    }


    @Override
    public boolean existComponentAccessToken() {
        return exist(ApplicationConfig.KEY_WEIXIN_COMPONENT_ACCESS_TOKEN);
    }

    @Override
    public boolean equalComponentAccessToken(String componentAccessToken) {
        return equal(ApplicationConfig.KEY_WEIXIN_COMPONENT_ACCESS_TOKEN, componentAccessToken);
    }

    @Override
    public String getPreAuthCode() {
        return getData(ApplicationConfig.KEY_WEIXIN_PRE_AUTH_CODE);
    }

    @Override
    public void setPreAuthCode(String preAuthCode) {
        setData(ApplicationConfig.KEY_WEIXIN_PRE_AUTH_CODE, preAuthCode, 19 * 60);
    }

    @Override
    public boolean existPreAuthCode() {
        return exist(ApplicationConfig.KEY_WEIXIN_PRE_AUTH_CODE);
    }

    @Override
    public boolean equalPreAuthCode(String preAuthCode) {
        return equal(ApplicationConfig.KEY_WEIXIN_PRE_AUTH_CODE, preAuthCode);
    }
//
//    @Override
//    public String getPreAuthCode() {
//        return redisUtil.get(Config.REDIS_PREFIX + Config.KEY_WEIXIN_PRE_AUTH_CODE);
//    }
//
//    @Override
//    public void setPreAuthCode(String preAuthCode) {
//        redisUtil.setEx(Config.REDIS_PREFIX + Config.KEY_WEIXIN_PRE_AUTH_CODE, 19 * 60, preAuthCode);
//    }
//
//
//    @Override
//    public boolean existPreAuthCode() {
//        return redisUtil.exists(Config.REDIS_PREFIX + Config.KEY_WEIXIN_PRE_AUTH_CODE);
//    }
//
//    @Override
//    public boolean equalPreAuthCode(String preAuthCode) {
//        return equal(Config.REDIS_PREFIX + Config.KEY_WEIXIN_PRE_AUTH_CODE, preAuthCode);
//    }
//
//    @Override
//    public String getAccessToken(Long siteId) {
//        String key = Config.REDIS_PREFIX + Config.KEY_WEIXIN_ACCESS_TOKEN + siteId;
//        return redisUtil.get(key);
//    }
//
//    @Override
//    public void setAccessToken(Long siteId, String accessToken) {
//        String key = Config.REDIS_PREFIX + Config.KEY_WEIXIN_ACCESS_TOKEN + siteId;
//        setData(key, accessToken, 7000);
//    }
//
//    @Override
//    public boolean existAccessToken(Long siteId) {
//        String key = Config.REDIS_PREFIX + Config.KEY_WEIXIN_ACCESS_TOKEN + siteId;
//        return exist(key);
//    }
//
//    @Override
//    public boolean equalAccessToken(Long siteId, String accessToken) {
//        String key = Config.REDIS_PREFIX + Config.KEY_WEIXIN_ACCESS_TOKEN + siteId;
//        return equal(key, accessToken);
//    }
//
//    @Override
//    public void deleteAccessToken(Long siteId) {
//        String key = Config.REDIS_PREFIX + Config.KEY_WEIXIN_ACCESS_TOKEN + siteId;
//        deleteData(key);
//    }
//
//    @Override
//    public String getAppId(Long siteId) {
//        String key = Config.REDIS_PREFIX + Config.KEY_WEIXIN_APPID + siteId;
//        return redisUtil.get(key);
//    }
//
//    @Override
//    public void setAppId(Long siteId, String appId) {
//        String key = Config.REDIS_PREFIX + Config.KEY_WEIXIN_APPID + siteId;
//        setData(key, appId, 24 * 60 * 60);
//    }
//
//    @Override
//    public boolean existAppId(Long siteId) {
//        String key = Config.REDIS_PREFIX + Config.KEY_WEIXIN_APPID + siteId;
//        return exist(key);
//    }
//
//    @Override
//    public void deleteAppId(Long siteId) {
//        String key = Config.REDIS_PREFIX + Config.KEY_WEIXIN_APPID + siteId;
//        deleteData(key);
//    }
//
//    @Override
//    public boolean equalAppId(Long siteId, String appId) {
//        String key = Config.REDIS_PREFIX + Config.KEY_WEIXIN_APPID + siteId;
//        return equal(key, appId);
//    }
}
