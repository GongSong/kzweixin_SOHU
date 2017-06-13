package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.config.WxApiConfig;
import com.kuaizhan.constant.ErrorCode;
import com.kuaizhan.dao.mapper.AccountDao;
import com.kuaizhan.dao.mapper.UnbindDao;
import com.kuaizhan.dao.redis.RedisAccountDao;
import com.kuaizhan.exception.BusinessException;
import com.kuaizhan.exception.common.DaoException;
import com.kuaizhan.exception.common.RedisException;
import com.kuaizhan.exception.kuaizhan.KzApiException;
import com.kuaizhan.exception.weixin.WxIPNotInWhitelistException;
import com.kuaizhan.exception.weixin.WxInvalidAppSecretException;
import com.kuaizhan.manager.WxAccountManager;
import com.kuaizhan.manager.KzManager;
import com.kuaizhan.pojo.po.AccountPO;
import com.kuaizhan.pojo.po.UnbindPO;
import com.kuaizhan.pojo.dto.AuthorizationInfoDTO;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.WeixinAuthService;
import com.kuaizhan.utils.DateUtil;
import com.kuaizhan.utils.IdGeneratorUtil;
import com.kuaizhan.utils.UrlUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by liangjiateng on 2017/3/15.
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Resource
    private RedisAccountDao redisAccountDao;
    @Resource
    private AccountDao accountDao;
    @Resource
    private UnbindDao unbindDao;
    @Resource
    private WeixinAuthService weixinAuthService;

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);


    @Override
    public String getBindUrl(Long userId, Long siteId) {
        String redirectUrl = ApplicationConfig.KZ_DOMAIN_MAIN + "/weixin/account-bind-callback?userId=" + userId;
        // 同时绑定站点
        if (siteId != null) {
            redirectUrl += "&siteId=" + siteId;
        }
        redirectUrl = UrlUtil.encode(redirectUrl);
        String preAuthCode = weixinAuthService.getPreAuthCode();

        return WxApiConfig.getBindUrl(preAuthCode, redirectUrl);
    }

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
            throw new BusinessException(ErrorCode.SITE_ID_NOT_EXIST);
        }
        return accountPO;
    }

    @Override
    public AccountPO getAccountByAppId(String appId) {
        AccountPO accountPO = accountDao.getAccountByAppId(appId);
        if (accountPO == null) {
            throw new BusinessException(ErrorCode.APP_ID_NOT_EXIST);
        }
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
                throw new BusinessException(ErrorCode.ACCOUNT_NOT_EXIST);
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
            account.setUnbindTime((long) DateUtil.curSeconds());
            account.setIsDel(1);
            accountDao.updateAccountBySiteId(account);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void updateAppSecret(long weixinAppId, String appSecret) {
        AccountPO accountPO = getAccountByWeixinAppId(weixinAppId);

        //调用WxAccountManager，把用户ID和app_secret发给微信服务器接口，获取access token并验证
        try {
            WxAccountManager.getAccessToken(accountPO.getAppId(), appSecret);
        } catch (WxIPNotInWhitelistException e) {
            throw new BusinessException(ErrorCode.IP_NOT_IN_WHITELIST);
        } catch (WxInvalidAppSecretException e) {
            throw new BusinessException(ErrorCode.INVALID_APP_SECRET);
        }

        AccountPO updatePO = new AccountPO();
        updatePO.setWeixinAppId(weixinAppId);
        updatePO.setAppSecret(appSecret);
        accountDao.updateAccountByWeixinAppId(updatePO);
    }

    @Override
    public void updateCustomizeShare(long weixinAppId, Integer openShare) {
        AccountPO accountPO = getAccountByWeixinAppId(weixinAppId);
        JSONObject jsonObject;

        //检测数据库是否存在记录，并更新自定义分享状态
        if (accountPO.getAdvancedFuncInfoJson() == "") {
            jsonObject = new JSONObject();
            jsonObject.put("open_login", 0);
        } else {
            jsonObject = new JSONObject(accountPO.getAdvancedFuncInfoJson());
        }
        jsonObject.put("open_share", openShare);

        AccountPO updatePO = new AccountPO();
        updatePO.setWeixinAppId(weixinAppId);
        updatePO.setAdvancedFuncInfoJson(jsonObject.toString());
        accountDao.updateAccountByWeixinAppId(updatePO);

    }

    @Override
    public void updateAuthLogin(long weixinAppId, Integer openLogin) {
        AccountPO accountPO = getAccountByWeixinAppId(weixinAppId);
        JSONObject jsonObject;

        //检测数据库是否存在记录
        if (accountPO.getAdvancedFuncInfoJson() == "") {
            jsonObject = new JSONObject();
            jsonObject.put("open_share", 0);
        } else {
            jsonObject = new JSONObject(accountPO.getAdvancedFuncInfoJson());
        }
        jsonObject.put("open_login", 0);

        //如果是服务号，进行验证。通过则保持服务号授权登录状态，不通过则关闭授权登录
        if (accountPO.getServiceType() == 2) {
            try {
                KzManager.kzAccountWxLoginCheck(accountPO.getSiteId());
            } catch (KzApiException e) {
                AccountPO updatePO = new AccountPO();
                updatePO.setWeixinAppId(weixinAppId);
                updatePO.setAdvancedFuncInfoJson(jsonObject.toString());
                accountDao.updateAccountByWeixinAppId(updatePO);
                throw new BusinessException(ErrorCode.NOT_SERVICE_NUMBER);
            }
            jsonObject.put("open_login", openLogin);
        }

        AccountPO updatePO = new AccountPO();
        updatePO.setWeixinAppId(weixinAppId);
        updatePO.setAdvancedFuncInfoJson(jsonObject.toString());
        accountDao.updateAccountByWeixinAppId(updatePO);

    }
}
