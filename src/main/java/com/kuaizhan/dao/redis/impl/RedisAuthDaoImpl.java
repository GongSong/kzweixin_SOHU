package com.kuaizhan.dao.redis.impl;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.redis.RedisAuthDao;
import org.json.JSONObject;
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
        String result = getData(ApplicationConfig.KEY_WEIXIN_COMPONENT_ACCESS_TOKEN);
        if (result == null) {
            return null;
        } else {
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject.getString("component_access_token");
        }
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


}
