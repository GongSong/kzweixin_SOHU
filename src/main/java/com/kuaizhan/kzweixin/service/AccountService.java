package com.kuaizhan.kzweixin.service;

import com.kuaizhan.kzweixin.dao.po.AccountPO;

/**
 * 账号服务
 * Created by liangjiateng on 2017/3/15.
 */
public interface AccountService {

    /**
     * 获取绑定公众号的url
     */
    String getBindUrl(Long userId, Long siteId);

    /**
     * 新增绑定公众号
     * @param userId 绑定的用户id
     * @param authCode 微信授权码
     * @param siteId 绑定的siteId, 允许为空
     */
    void bindAccount(Long userId, String authCode, Long siteId);

    /**
     * 获取accessToken
     */
    String getAccessToken(long weixinAppId);

    /**
     * 根据siteId获取账号信息
     */
    AccountPO getAccountBySiteId(long siteId);

    /**
     * 根据appId获取账号信息
     */
    AccountPO getAccountByAppId(String appId);

    /**
     * 根据long型Id获取账号信息
     */
    AccountPO getAccountByWeixinAppId(long weixinAppid);

    /**
     * 修改appSecret
     */
    void updateAppSecret(long weixinAppId, String appSecret);

    /**
     * 修改用户自定义分享开启／关闭状态
     * */
    void updateCustomizeShare(long weixinAppId, Integer openShare);

    /**
     * 修改服务号授权登录开启／关闭状态
     * */
    void updateAuthLogin(long weixinAppId, Integer openLogin);
}
