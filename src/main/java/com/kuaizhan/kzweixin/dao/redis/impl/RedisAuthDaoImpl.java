package com.kuaizhan.kzweixin.dao.redis.impl;

import com.kuaizhan.kzweixin.constant.RedisConstant;
import com.kuaizhan.kzweixin.dao.redis.RedisAuthDao;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;


/**
 * Created by Mr.Jadyn on 2017/1/19.
 */
@Repository("redisAuthDao")
public class RedisAuthDaoImpl extends RedisBaseDaoImpl implements RedisAuthDao {

    @Override
    public String getComponentVerifyTicket() {
        return getData(RedisConstant.KEY_WEIXIN_COMPONENT_VERIFY_TICKET);
    }

    @Override
    public void setComponentVerifyTicket(String ticket) {
        setData(RedisConstant.KEY_WEIXIN_COMPONENT_VERIFY_TICKET, ticket, 11 * 60);
    }

    @Override
    public String getComponentAccessToken() {
        String result = getData(RedisConstant.KEY_WEIXIN_COMPONENT_ACCESS_TOKEN);
        if (result == null) {
            return null;
        } else {
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject.getString("component_access_token");
        }
    }

    @Override
    public void setComponentAccessToken(String componentAccessToken) {
        setData(RedisConstant.KEY_WEIXIN_COMPONENT_ACCESS_TOKEN, componentAccessToken, 110 * 60);
    }
}
