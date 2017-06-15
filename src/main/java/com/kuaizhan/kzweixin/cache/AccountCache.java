package com.kuaizhan.kzweixin.cache;
import com.kuaizhan.kzweixin.dao.po.AccountPO;
import com.kuaizhan.kzweixin.entity.account.AccessTokenDTO;

/**
 * 账号缓存
 * Created by Mr.Jadyn on 2017/2/9.
 */
public interface AccountCache {


    AccountPO getAccountInfoByWeixinAppId(long weixinAppId);

    void setAccountInfo(AccountPO account);

    void deleteAccountInfo(long weixinAppId);

    /**
     * 获取accessToken缓存
     */
    String getAccessToken(long weixinAppId);

    /**
     * 缓存accessToken
     */
    void setAccessToken(long weixinAppId, AccessTokenDTO accessTokenDTO);
}
