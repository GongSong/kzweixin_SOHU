package com.kuaizhan.kzweixin.dao.mapper;

import com.kuaizhan.kzweixin.pojo.po.AccountPO;

/**
 * 账号dao
 * Created by Mr.Jadyn on 2017/1/25.
 */
public interface AccountDao {

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
     * @param appId 公众号appId
     * @return
     */
    AccountPO getAccountByAppId(String appId);

    /**
     * 根据weixinAppId获取账号信息
     *
     * @param weixinAppId
     * @return
     */
    AccountPO getAccountByWeixinAppId(long weixinAppId);

    /**
     * 查询已经删除的账户信息
     *
     * @param siteId 站点id
     * @return
     */
    AccountPO getDeleteAccountBySiteId(long siteId);

    /**
     * 添加一个账户
     *
     * @param account
     * @return
     */
    int insertAccount(AccountPO account);

    /**
     * 更新账户信息
     *
     * @param account
     * @return
     */
    int updateAccountBySiteId(AccountPO account);

    /**
     * 根据weixinAppId更新账户信息
     * @param account
     * @return
     */
    int updateAccountByWeixinAppId(AccountPO account);

    /**
     * 删除账户
     *
     * @param appId 公众号appId
     * @return
     */
    int deleteAccountByAppId(String appId);


}
