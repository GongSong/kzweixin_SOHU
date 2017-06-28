package com.kuaizhan.kzweixin.dao.mapper;

import com.kuaizhan.kzweixin.dao.po.auto.AccountPO;

/**
 * 账号dao
 * Created by Mr.Jadyn on 2017/1/25.
 */
public interface AccountDao {

    /**
     * 根据weixinAppId更新账户信息
     * @param account
     * @return
     */
    int updateAccountByWeixinAppId(AccountPO account);
}
