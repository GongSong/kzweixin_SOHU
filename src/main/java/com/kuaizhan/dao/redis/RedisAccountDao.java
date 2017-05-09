package com.kuaizhan.dao.redis;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DTO.AuthorizationInfoDTO;

import java.io.IOException;

/**
 * 账号缓存
 * Created by Mr.Jadyn on 2017/2/9.
 */
public interface RedisAccountDao {


    AccountDO getAccountInfoByWeixinAppId(long weixinAppId);

    void setAccountInfo(AccountDO account);

    void deleteAccountInfo(long weixinAppId);

    /**
     * 获取accessToken
     * @param weixinAppId
     * @return
     */
    String getAccessToken(long weixinAppId);

    void setAccessToken(long weixinAppId,AuthorizationInfoDTO authorizationInfoDTO);
}
