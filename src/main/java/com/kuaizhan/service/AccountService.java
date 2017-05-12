package com.kuaizhan.service;

import com.kuaizhan.exception.common.DaoException;
import com.kuaizhan.exception.common.RedisException;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DO.UnbindDO;

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
    void bindAccount(AccountDO account) throws RedisException, DaoException;

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
    AccountDO getAccountBySiteId(long siteId);

    /**
     * 根据appId获取账号信息
     *
     * @param appId
     * @return
     */
    AccountDO getAccountByAppId(String appId);

    /**
     * 根据long型Id获取账号信息
     *
     * @return
     */
    AccountDO getAccountByWeixinAppId(long weixinAppid);

    /**
     * 解绑账号
     *
     * @param account 账号
     */
    void unbindAccount(AccountDO account, UnbindDO unbindDO) throws RedisException, DaoException;

    /**
     * 修改appSecret
     *
     * @param siteId
     * @param appSecret
     * @return
     */
    void updateAppSecret(long siteId, String appSecret);
}
