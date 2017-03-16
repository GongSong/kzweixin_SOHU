package com.kuaizhan.service;

import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DO.UnbindDO;
import com.kuaizhan.pojo.VO.AccountVO;

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
     * 根据siteId获取账号信息
     *
     * @param siteId 站点id
     * @return
     */
    AccountDO getAccountBySiteId(long siteId) throws RedisException, DaoException;

    /**
     * 解绑账号
     *
     * @param account 账号
     */
    void unbindAccount(AccountDO account,UnbindDO unbindDO) throws RedisException, DaoException;
}
