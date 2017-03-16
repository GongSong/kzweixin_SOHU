package com.kuaizhan.service.impl;

import com.kuaizhan.dao.mapper.AccountDao;
import com.kuaizhan.dao.redis.RedisAccountDao;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.VO.AccountVO;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.utils.IdGeneratorUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by liangjiateng on 2017/3/15.
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Resource
    RedisAccountDao redisAccountDao;
    @Resource
    AccountDao accountDao;

    @Override
    public void bindAccount(AccountDO account) throws RedisException, DaoException {
        try {
            //删缓存
            redisAccountDao.deleteAccountInfo(account.getSiteId());
        } catch (Exception e) {
            throw new RedisException(e.getMessage());
        }
        AccountDO accountDO;
        try {
            //将其他公众号删除
            accountDao.deleteAccountByAppId(account.getAppId());
            //先查数据库存不存在
            accountDO = accountDao.getDeleteAccountBySiteId(account.getSiteId());
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
        //存在记录 恢复记录 更新数据库
        if (accountDO != null) {
            account.setIsDel(0);
            try {
                accountDao.updateAccountBySiteId(account);
            } catch (Exception e) {
                throw new DaoException(e.getMessage());
            }
            //刷新缓存
            try {
                redisAccountDao.setAccountInfo(accountDO);
            } catch (Exception e) {
                throw new RedisException(e.getMessage());
            }
        }
        //不存在则创建新的数据
        else {
            account.setWeixinAppId(IdGeneratorUtil.getID());
            account.setIsDel(0);
            try {
                accountDao.insertAccount(account);
            } catch (Exception e) {
                throw new DaoException(e.getMessage());
            }
            //刷新缓存
            try {
                redisAccountDao.setAccountInfo(accountDO);
            } catch (Exception e) {
                throw new RedisException(e.getMessage());
            }
        }
    }

    @Override
    public AccountVO getAccountBySiteId(long siteId) throws RedisException, DaoException {
        AccountDO accountDO;
        AccountVO accountVO = null;
        //从缓存拿
        try {
            accountDO = redisAccountDao.getAccountInfo(siteId);
        } catch (Exception e) {
            throw new RedisException(e.getMessage());
        }
        if (accountDO == null) {
            //从数据库拿
            accountDO = accountDao.getAccountBySiteId(siteId);
        }

        //存缓存
        if (accountDO != null) {
            accountVO = new AccountVO();
            accountVO.setAppId(accountDO.getWeixinAppId());
            accountVO.setAppSecret(accountDO.getAppSecret());
            accountVO.setHeadImg(accountDO.getHeadImg());
            accountVO.setInterest(accountDO.getInterestJson());
            accountVO.setName(accountDO.getNickName());
            accountVO.setQrcode(accountDO.getQrcodeUrl());
            accountVO.setType(accountDO.getServiceType());

        }
        try {
            redisAccountDao.setAccountInfo(accountDO);
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
        return accountVO;
    }
}
