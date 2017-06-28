package com.kuaizhan.kzweixin.cache;
import com.kuaizhan.kzweixin.dao.po.auto.AccountPO;
import com.kuaizhan.kzweixin.entity.account.AccessTokenDTO;

/**
 * 账号缓存
 * Created by Mr.Jadyn on 2017/2/9.
 */
public interface AccountCache {


    /**
     * 根据weixinAppId对公众号信息的缓存
     */
    AccountPO getAccountByWeixinAppid(long weixinAppid);
    void setAccountByWeixinAppid(AccountPO accountPO);
    void deleteAccountByWeixinAppid(long weixinAppid);

    /**
     * 根据appId对公众号信息缓存
     */
    AccountPO getAccountByAppid(String appid);
    void setAccountByAppid(AccountPO accountPO);
    void deleteAccountByAppid(String appid);

    /**
     * 对accessToken的缓存
     */
    String getAccessToken(long weixinAppid);
    void setAccessToken(long weixinAppid, AccessTokenDTO accessTokenDTO);
    void deleteAccessToken(long weixinAppid);


    /* ----------- php缓存  ------------- */
    /**
     * 删除php那边根据siteId对account的缓存
     */
    void deletePhpAccountBySiteId(long siteId);
}
