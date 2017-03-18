package com.kuaizhan.dao.redis;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuaizhan.pojo.DO.AccountDO;

import java.io.IOException;

/**
 * 账号缓存
 * Created by Mr.Jadyn on 2017/2/9.
 */
public interface RedisAccountDao {

    AccountDO getAccountInfo(long siteId) throws IOException;

    void setAccountInfo(AccountDO account) throws JsonProcessingException;

    void deleteAccountInfo(long siteId);
}
