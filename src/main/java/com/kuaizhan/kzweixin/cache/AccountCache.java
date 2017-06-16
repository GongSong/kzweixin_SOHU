package com.kuaizhan.kzweixin.cache;
import com.kuaizhan.kzweixin.dao.po.AccountPO;
import com.kuaizhan.kzweixin.entity.account.AccessTokenDTO;

/**
 * 账号缓存
 * Created by Mr.Jadyn on 2017/2/9.
 */
public interface AccountCache {


    /**
     * 对用户信息的缓存
     */
    AccountPO getAccount(long weixinAppid);
    void setAccount(AccountPO account);
    void deleteAccount(long weixinAppid);

    /**
     * 对accessToken的缓存
     */
    String getAccessToken(long weixinAppid);
    void setAccessToken(long weixinAppid, AccessTokenDTO accessTokenDTO);
    void deleteAccessToken(long weixinAppid);
}
