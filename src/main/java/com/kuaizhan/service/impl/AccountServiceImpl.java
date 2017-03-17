package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.mapper.AccountDao;
import com.kuaizhan.dao.mapper.UnbindDao;
import com.kuaizhan.dao.redis.RedisAccountDao;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DO.UnbindDO;
import com.kuaizhan.pojo.DTO.AuthorizationInfoDTO;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.WeixinAuthService;
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
    @Resource
    UnbindDao unbindDao;
    @Resource
    WeixinAuthService weixinAuthService;

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
            account.setUnbindTime(0L);
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
        }
    }

    @Override
    public AccountDO getAccountBySiteId(long siteId) throws RedisException, DaoException {
        AccountDO accountDO;
        //从缓存拿
        try {
            accountDO = redisAccountDao.getAccountInfo(siteId);

        } catch (Exception e) {
            throw new RedisException(e.getMessage());
        }
        if (accountDO == null) {
            //从数据库拿
            try {
                accountDO = accountDao.getAccountBySiteId(siteId);
                //查看access_token是否失效
                if (System.currentTimeMillis() / 1000 > accountDO.getExpiresTime()) {
                    AuthorizationInfoDTO authorizationInfoDTO = weixinAuthService.refreshAuthorizationInfo(weixinAuthService.getComponentAccessToken(), ApplicationConfig.WEIXIN_APPID_THIRD, accountDO.getAppId(), accountDO.getRefreshToken());
                    accountDO.setAccessToken(authorizationInfoDTO.getAccessToken());
                    accountDO.setRefreshToken(authorizationInfoDTO.getRefreshToken());
                    accountDao.updateAccountBySiteId(accountDO);
                }
            } catch (Exception e) {
                throw new DaoException(e.getMessage());
            }
            //存缓存
            try {
                redisAccountDao.setAccountInfo(accountDO);
            } catch (Exception e) {
                throw new RedisException(e.getMessage());
            }
        }
        return accountDO;
    }

    @Override
    public void unbindAccount(AccountDO account, UnbindDO unbindDO) throws RedisException, DaoException {
        UnbindDO unbind;
        //删缓存
        try {
            redisAccountDao.deleteAccountInfo(account.getSiteId());
        } catch (Exception e) {
            throw new RedisException(e.getMessage());
        }
        try {
            unbind = unbindDao.getUnbindByWeixinAppId(account.getWeixinAppId());
            unbindDO.setWeixinAppId(account.getWeixinAppId());
            if (unbind == null) {
                unbindDao.insertUnbind(unbindDO);
            } else {
                unbindDao.updateUnbindByWeixinAppId(unbindDO);
            }
            account.setUnbindTime(System.currentTimeMillis() / 1000);
            account.setIsDel(1);
            accountDao.updateAccountBySiteId(account);
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void updateAppSecrect(long siteId, String appSecret) throws DaoException {
        AccountDO account = new AccountDO();
        account.setSiteId(siteId);
        account.setAppSecret(appSecret);
        try {
            accountDao.updateAccountBySiteId(account);
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }

    }
}
