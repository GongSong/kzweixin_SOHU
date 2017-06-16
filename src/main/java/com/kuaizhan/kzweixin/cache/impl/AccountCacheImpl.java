package com.kuaizhan.kzweixin.cache.impl;

import com.kuaizhan.kzweixin.constant.RedisConstant;
import com.kuaizhan.kzweixin.cache.AccountCache;

import com.kuaizhan.kzweixin.dao.po.AccountPO;
import com.kuaizhan.kzweixin.entity.account.AccessTokenDTO;
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
    public AccountPO getAccount(long weixinAppid) {
        String key = RedisConstant.KEY_ACCOUNT + weixinAppid;
        String result = getData(key);
        if (result == null) {
            return null;
        }
        return JsonUtil.string2Bean(result, AccountPO.class);
    }


    @Override
    public void setAccount(AccountPO account) {
        String key = RedisConstant.KEY_ACCOUNT + account.getWeixinAppId();
        String str = JsonUtil.bean2String(account);
        
        // TODO: 因为修改账号信息还在php代码那边，缓存时间设置得很短，账号模块完全重构后修改
        setData(key, str, 60);
    }

    @Override
    public void deleteAccount(long weixinAppid) {
        deleteData(RedisConstant.KEY_ACCOUNT + weixinAppid);
    }

    @Override
    public String getAccessToken(long weixinAppid) {
        String key = RedisConstant.KEY_WEIXIN_USER_ACCESS_TOKEN + weixinAppid;
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
    public void setAccessToken(long weixinAppid, AccessTokenDTO accessTokenDTO) {
        if (accessTokenDTO != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("access_token", accessTokenDTO.getAccessToken());
            jsonObject.put("refresh_token", accessTokenDTO.getRefreshToken());
            jsonObject.put("expires_time", DateUtil.curSeconds() + accessTokenDTO.getExpiresIn() - 10 * 60);
            setData(RedisConstant.KEY_WEIXIN_USER_ACCESS_TOKEN + weixinAppid, jsonObject.toString(), 2 * 60 * 60);
        }
    }

    @Override
    public void deleteAccessToken(long weixinAppid) {
        deleteData(RedisConstant.KEY_WEIXIN_USER_ACCESS_TOKEN + weixinAppid);
    }

    @Override
    public void deletePhpAccountBySiteId(long siteId) {
        deleteData(RedisConstant.KEY_PHP_ACCOUNT_BY_SITE_ID + siteId);
    }
}
