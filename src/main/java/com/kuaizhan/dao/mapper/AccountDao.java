package com.kuaizhan.dao.mapper;

import com.kuaizhan.pojo.DO.AccountDO;

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
    AccountDO getAccountBySiteId(long siteId);

    /**
     * 根据appId获取账号信息
     *
     * @param appId 公众号appId
     * @return
     */
    AccountDO getAccountByAppId(String appId);

    /**
     * 查询已经删除的账户信息
     *
     * @param siteId 站点id
     * @return
     */
    AccountDO getDeleteAccountBySiteId(long siteId);

    /**
     * 添加一个账户
     *
     * @param account
     * @return
     */
    int insertAccount(AccountDO account);

    /**
     * 更新账户信息
     *
     * @param account
     * @return
     */
    int updateAccountBySiteId(AccountDO account);

    /**
     * 删除账户
     *
     * @param appId 公众号appId
     * @return
     */
    int deleteAccountByAppId(String appId);



}
