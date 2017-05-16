package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.constant.ErrorCodes;
import com.kuaizhan.dao.mapper.AccountDao;
import com.kuaizhan.dao.mapper.UnbindDao;
import com.kuaizhan.dao.redis.RedisAccountDao;
import com.kuaizhan.exception.BusinessException;
import com.kuaizhan.exception.common.DaoException;
import com.kuaizhan.exception.common.RedisException;
import com.kuaizhan.pojo.po.AccountPO;
import com.kuaizhan.pojo.po.UnbindPO;
import com.kuaizhan.pojo.dto.AuthorizationInfoDTO;
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
    public void bindAccount(AccountPO account) throws RedisException, DaoException {
        try {
            //删缓存
            redisAccountDao.deleteAccountInfo(account.getSiteId());
        } catch (Exception e) {
            throw new RedisException(e);
        }
        AccountPO accountPO;
        try {
            //将其他公众号删除
            accountDao.deleteAccountByAppId(account.getAppId());
            //先查数据库存不存在
            accountPO = accountDao.getDeleteAccountBySiteId(account.getSiteId());
        } catch (Exception e) {
            throw new DaoException(e);
        }
        //存在记录 恢复记录 更新数据库
        if (accountPO != null) {
            account.setIsDel(0);
            account.setUnbindTime(0L);
            try {
                accountDao.updateAccountBySiteId(account);
            } catch (Exception e) {
                throw new DaoException(e);
            }
            //刷新缓存
            try {
                redisAccountDao.setAccountInfo(accountPO);
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
    public String getAccessToken(long weixinAppId) {
        //存在高并发场景下access_token失效的问题
        String accessToken = redisAccountDao.getAccessToken(weixinAppId);
        // TODO: 在这里实现分布式锁，否则在消息队列和web同时调用的情况下，有一定几率相互覆盖
        if (accessToken == null) {
            AccountPO accountPO = getAccountByWeixinAppId(weixinAppId);
            //刷新
            AuthorizationInfoDTO authorizationInfoDTO = weixinAuthService.refreshAuthorizationInfo(
                    weixinAuthService.getComponentAccessToken(),
                    ApplicationConfig.WEIXIN_APPID_THIRD, accountPO.getAppId(),
                    accountPO.getRefreshToken());
            accessToken = authorizationInfoDTO.getAccessToken();

            // 更新数据库
            AccountPO updateAccountPO = new AccountPO();
            updateAccountPO.setWeixinAppId(accountPO.getWeixinAppId());
            updateAccountPO.setAccessToken(authorizationInfoDTO.getAccessToken());
            updateAccountPO.setRefreshToken(authorizationInfoDTO.getRefreshToken());
            accountDao.updateAccountByWeixinAppId(updateAccountPO);
            //设置缓存
            redisAccountDao.setAccessToken(weixinAppId, authorizationInfoDTO);
        }
        return accessToken;
    }

    @Override
    public AccountPO getAccountBySiteId(long siteId) {
        AccountPO accountPO = accountDao.getAccountBySiteId(siteId);
        if (accountPO == null) {
            throw new BusinessException(ErrorCodes.SITE_ID_NOT_EXIST);
        }
        return accountPO;
    }

    @Override
    public AccountPO getAccountByAppId(String appId) {
        return accountDao.getAccountByAppId(appId);
    }

    @Override
    public AccountPO getAccountByWeixinAppId(long weinxinAppid) {
        // 从缓存拿
        AccountPO accountPO = redisAccountDao.getAccountInfoByWeixinAppId(weinxinAppid);

        if (accountPO == null) {
            //从数据库拿
            accountPO = accountDao.getAccountByWeixinAppId(weinxinAppid);
            if (accountPO == null) {
                throw new BusinessException(ErrorCodes.ACCOUNT_NOT_EXIST);
            }

            //存缓存
            redisAccountDao.setAccountInfo(accountPO);
        }
        return accountPO;
    }

    @Override
    public void unbindAccount(AccountPO account, UnbindPO unbindPO) {
        UnbindPO unbind;
        //删缓存
        try {
            redisAccountDao.deleteAccountInfo(account.getSiteId());
        } catch (Exception e) {
            throw new RedisException(e);
        }
        try {
            unbind = unbindDao.getUnbindByWeixinAppId(account.getWeixinAppId());
            unbindPO.setWeixinAppId(account.getWeixinAppId());
            if (unbind == null) {
                unbindDao.insertUnbind(unbindPO);
            } else {
                unbindDao.updateUnbindByWeixinAppId(unbindPO);
            }
            account.setUnbindTime(System.currentTimeMillis() / 1000);
            account.setIsDel(1);
            accountDao.updateAccountBySiteId(account);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void updateAppSecret(long siteId, String appSecret) {
        AccountPO account = new AccountPO();
        account.setSiteId(siteId);
        account.setAppSecret(appSecret);
        accountDao.updateAccountBySiteId(account);
    }
}
