package com.kuaizhan.service;

import com.kuaizhan.exception.common.DaoException;
import com.kuaizhan.exception.common.RedisException;
import com.kuaizhan.pojo.po.AccountPO;
import com.kuaizhan.pojo.po.UnbindPO;

/**
 * 账号服务
 * Created by liangjiateng on 2017/3/15.
 */
public interface AccountService {
    /**
     * 绑定账号
     *
     * @param account 账号
     * @return
     */
    void bindAccount(AccountPO account) throws RedisException, DaoException;

    /**
     * 获取accessToken
     *
     * @param weixinAppId
     * @return
     */
    String getAccessToken(long weixinAppId);

    /**
     * 根据siteId获取账号信息
     *
     * @param siteId 站点id
     * @return
     */
    AccountPO getAccountBySiteId(long siteId);

    /**
     * 根据appId获取账号信息
     *
     * @param appId
     * @return
     */
    AccountPO getAccountByAppId(String appId);

    /**
     * 根据long型Id获取账号信息
     *
     * @return
     */
    AccountPO getAccountByWeixinAppId(long weixinAppid);

    /**
     * 解绑账号
     *
     * @param account 账号
     */
    void unbindAccount(AccountPO account, UnbindPO unbindPO) throws RedisException, DaoException;

    /**
     * 修改appSecret
     *
     * @param siteId
     * @param appSecret
     * @return
     */
    void updateAppSecret(long siteId, String appSecret);
}
