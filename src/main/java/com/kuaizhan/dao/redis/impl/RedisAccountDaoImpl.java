package com.kuaizhan.dao.redis.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.redis.RedisAccountDao;

import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DTO.AuthorizationInfoDTO;
import com.kuaizhan.utils.JsonUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.io.IOException;


/**
 * Created by Mr.Jadyn on 2017/2/9.
 */
@Repository("redisAccountDao")
public class RedisAccountDaoImpl extends RedisBaseDaoImpl implements RedisAccountDao {


    @Override
    public AccountDO getAccountInfoByWeixinAppId(long weixinAppId) throws IOException {
        String key = ApplicationConfig.KEY_ACCOUNT_INFO + weixinAppId;
        String result = getData(key);
        if (result == null) {
            return null;
        } else {
            return JsonUtil.string2Bean(result, AccountDO.class);
        }
    }


    @Override
    public void setAccountInfo(AccountDO account) throws JsonProcessingException {
        String key = ApplicationConfig.KEY_ACCOUNT_INFO + account.getWeixinAppId();
        String str = JsonUtil.bean2String(account);
        setData(key, str, 7000);
    }

    @Override
    public void deleteAccountInfo(long weixinAppId) {
        deleteData(ApplicationConfig.KEY_ACCOUNT_INFO + weixinAppId);
    }

    @Override
    public String getAccessToken(long weixinAppId) {
        String key = ApplicationConfig.KEY_WEIXIN_USER_ACCESS_TOKEN + weixinAppId;
        String result = getData(key);
        if (result == null) {
            return null;
        } else {
            JSONObject jsonObject = new JSONObject(result);
            long expires = jsonObject.getInt("expires_time");
            if (expires - getTtl(key) < 10 * 60) {
                return null;
            }
            return jsonObject.getString("access_token");

        }
    }

    @Override
    public void setAccessToken(long weixinAppId, AuthorizationInfoDTO authorizationInfoDTO) {
        if (authorizationInfoDTO != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("access_token", authorizationInfoDTO.getAccessToken());
            jsonObject.put("refresh_token", authorizationInfoDTO.getRefreshToken());
            jsonObject.put("expires_time", authorizationInfoDTO.getExpiresIn());
            setData(ApplicationConfig.KEY_WEIXIN_USER_ACCESS_TOKEN + weixinAppId, jsonObject.toString(), 2 * 60 * 60);
        }
    }

    @Override
    public void deleteAccessToken(long weixinAppId) {
        deleteData(ApplicationConfig.KEY_WEIXIN_USER_ACCESS_TOKEN + weixinAppId);
    }

    @Override
    public boolean equalAccessToken(long weixinAppId, String accessToken) {
        return equal(ApplicationConfig.KEY_WEIXIN_USER_ACCESS_TOKEN + weixinAppId, accessToken);
    }

}
