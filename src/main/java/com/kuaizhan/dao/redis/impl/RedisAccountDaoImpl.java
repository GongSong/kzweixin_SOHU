package com.kuaizhan.dao.redis.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuaizhan.constant.RedisConstant;
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
        String key = RedisConstant.KEY_ACCOUNT_INFO + weixinAppId;
        String result = getData(key);
        if (result == null) {
            return null;
        } else {
            return JsonUtil.string2Bean(result, AccountDO.class);
        }
    }


    @Override
    public void setAccountInfo(AccountDO account) throws JsonProcessingException {
        String key = RedisConstant.KEY_ACCOUNT_INFO + account.getWeixinAppId();
        String str = JsonUtil.bean2String(account);
        setData(key, str, 7000);
    }

    @Override
    public void deleteAccountInfo(long weixinAppId) {
        deleteData(RedisConstant.KEY_ACCOUNT_INFO + weixinAppId);
    }

    @Override
    public String getAccessToken(long weixinAppId) {
        String key = RedisConstant.KEY_WEIXIN_USER_ACCESS_TOKEN + weixinAppId;
        String result = getData(key);
        if (result == null) {
            return null;
        } else {
            JSONObject jsonObject = new JSONObject(result);
            if (getTtl(key) < 10 * 60) {
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
            jsonObject.put("expires_time", System.currentTimeMillis() / 1000 + authorizationInfoDTO.getExpiresIn() - 10 * 60);
            setData(RedisConstant.KEY_WEIXIN_USER_ACCESS_TOKEN + weixinAppId, jsonObject.toString(), 2 * 60 * 60);
        }
    }
}
