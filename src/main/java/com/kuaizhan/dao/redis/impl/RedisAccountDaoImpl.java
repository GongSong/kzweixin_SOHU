package com.kuaizhan.dao.redis.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.redis.RedisAccountDao;

import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.utils.JsonUtil;
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
}
