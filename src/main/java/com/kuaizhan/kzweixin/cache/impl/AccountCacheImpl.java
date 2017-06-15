package com.kuaizhan.kzweixin.cache.impl;

import com.kuaizhan.kzweixin.constant.RedisConstant;
import com.kuaizhan.kzweixin.cache.AccountCache;

import com.kuaizhan.kzweixin.dao.po.AccountPO;
import com.kuaizhan.kzweixin.entity.account.AccessTokenDTO;
import com.kuaizhan.kzweixin.entity.account.AuthorizationInfoDTO;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;


/**
 * Created by Mr.Jadyn on 2017/2/9.
 */
@Repository("accountCache")
public class AccountCacheImpl extends RedisBaseDaoImpl implements AccountCache {


    @Override
    public AccountPO getAccountInfoByWeixinAppId(long weixinAppId) {
        String key = RedisConstant.KEY_ACCOUNT_INFO + weixinAppId;
        String result = getData(key);
        if (result == null) {
            return null;
        }
        return JsonUtil.string2Bean(result, AccountPO.class);
    }


    @Override
    public void setAccountInfo(AccountPO account) {
        String key = RedisConstant.KEY_ACCOUNT_INFO + account.getWeixinAppId();
        String str = JsonUtil.bean2String(account);
        // TODO: 因为修改账号信息还在php代码那边，缓存时间设置得很短，账号模块完全重构后修改
        setData(key, str, 60);
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
        }

        JSONObject jsonObject = new JSONObject(result);
        if (getTtl(key) < 10 * 60) {
            return null;
        }
        return jsonObject.getString("access_token");
    }

    @Override
    public void setAccessToken(long weixinAppId, AccessTokenDTO accessTokenDTO) {
        if (accessTokenDTO != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("access_token", accessTokenDTO.getAccessToken());
            jsonObject.put("refresh_token", accessTokenDTO.getRefreshToken());
            jsonObject.put("expires_time", DateUtil.curSeconds() + accessTokenDTO.getExpiresIn() - 10 * 60);
            setData(RedisConstant.KEY_WEIXIN_USER_ACCESS_TOKEN + weixinAppId, jsonObject.toString(), 2 * 60 * 60);
        }
    }
}
