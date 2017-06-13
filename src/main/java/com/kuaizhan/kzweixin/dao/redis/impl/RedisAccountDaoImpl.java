package com.kuaizhan.kzweixin.dao.redis.impl;

import com.kuaizhan.kzweixin.constant.RedisConstant;
import com.kuaizhan.kzweixin.dao.redis.RedisAccountDao;

import com.kuaizhan.kzweixin.pojo.po.AccountPO;
import com.kuaizhan.kzweixin.pojo.dto.AuthorizationInfoDTO;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;


/**
 * Created by Mr.Jadyn on 2017/2/9.
 */
@Repository("redisAccountDao")
public class RedisAccountDaoImpl extends RedisBaseDaoImpl implements RedisAccountDao {


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
            jsonObject.put("expires_time", DateUtil.curSeconds() + authorizationInfoDTO.getExpiresIn() - 10 * 60);
            setData(RedisConstant.KEY_WEIXIN_USER_ACCESS_TOKEN + weixinAppId, jsonObject.toString(), 2 * 60 * 60);
        }
    }
}
