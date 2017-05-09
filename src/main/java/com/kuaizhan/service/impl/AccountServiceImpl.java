package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.mapper.AccountDao;
import com.kuaizhan.dao.mapper.UnbindDao;
import com.kuaizhan.dao.redis.RedisAccountDao;
import com.kuaizhan.exception.business.AccountNotExistException;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.JsonParseException;
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
            throw new RedisException(e);
        }
        AccountDO accountDO;
        try {
            //将其他公众号删除
            accountDao.deleteAccountByAppId(account.getAppId());
            //先查数据库存不存在
            accountDO = accountDao.getDeleteAccountBySiteId(account.getSiteId());
        } catch (Exception e) {
            throw new DaoException(e);
        }
        //存在记录 恢复记录 更新数据库
        if (accountDO != null) {
            account.setIsDel(0);
            account.setUnbindTime(0L);
            try {
                accountDao.updateAccountBySiteId(account);
            } catch (Exception e) {
                throw new DaoException(e);
            }
            //刷新缓存
            try {
                redisAccountDao.setAccountInfo(accountDO);
            } catch (Exception e) {
                throw new RedisException(e);
            }
        }
        //不存在则创建新的数据
        else {
            account.setWeixinAppId(IdGeneratorUtil.getID());
            account.setIsDel(0);
            try {
                accountDao.insertAccount(account);
            } catch (Exception e) {
                throw new DaoException(e);
            }
        }
    }

    @Override
    public String getAccessToken(long weixinAppId) throws AccountNotExistException {
        //存在高并发场景下access_token失效的问题
        String accessToken = redisAccountDao.getAccessToken(weixinAppId);
        // TODO: 在这里实现分布式锁，否则在消息队列和web同时调用的情况下，有一定几率相互覆盖
        if (accessToken == null) {
            //从数据库拿refresh key刷新
            AccountDO accountDO  = accountDao.getAccountByWeixinAppId(weixinAppId);
            if (accountDO == null) {
                throw new AccountNotExistException();
            }
            //刷新
            AuthorizationInfoDTO authorizationInfoDTO = weixinAuthService.refreshAuthorizationInfo(
                    weixinAuthService.getComponentAccessToken(),
                    ApplicationConfig.WEIXIN_APPID_THIRD, accountDO.getAppId(),
                    accountDO.getRefreshToken());
            accessToken = authorizationInfoDTO.getAccessToken();

            // 更新数据库
            AccountDO updateAccountDo = new AccountDO();
            updateAccountDo.setWeixinAppId(accountDO.getWeixinAppId());
            updateAccountDo.setAccessToken(authorizationInfoDTO.getAccessToken());
            updateAccountDo.setRefreshToken(authorizationInfoDTO.getRefreshToken());
            accountDao.updateAccountByWeixinAppId(updateAccountDo);
            //设置缓存
            redisAccountDao.setAccessToken(weixinAppId, authorizationInfoDTO);
        }
        return accessToken;
    }

    @Override
    public AccountDO getAccountBySiteId(long siteId) throws RedisException, DaoException, AccountNotExistException, JsonParseException {
        AccountDO accountDO;
        try {
            accountDO = accountDao.getAccountBySiteId(siteId);
        } catch (Exception e) {
            throw new DaoException(e);
        }
        if (accountDO == null) {
            throw new AccountNotExistException();
        }
        return accountDO;
    }

    @Override
    public AccountDO getAccountByAppId(String appId) throws DaoException {
        try {
            return accountDao.getAccountByAppId(appId);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public AccountDO getAccountByWeixinAppId(long weinxinAppid) throws AccountNotExistException {
        // 从缓存拿
        AccountDO accountDO = redisAccountDao.getAccountInfoByWeixinAppId(weinxinAppid);

        if (accountDO == null) {
            //从数据库拿
            accountDO = accountDao.getAccountByWeixinAppId(weinxinAppid);
            if (accountDO == null) {
                throw new AccountNotExistException();
            }

            //存缓存
            redisAccountDao.setAccountInfo(accountDO);
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
            throw new RedisException(e);
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
            throw new DaoException(e);
        }
    }

    @Override
    public void updateAppSecret(long siteId, String appSecret) throws DaoException {
        AccountDO account = new AccountDO();
        account.setSiteId(siteId);
        account.setAppSecret(appSecret);
        try {
            accountDao.updateAccountBySiteId(account);
        } catch (Exception e) {
            throw new DaoException(e);
        }

    }
}
