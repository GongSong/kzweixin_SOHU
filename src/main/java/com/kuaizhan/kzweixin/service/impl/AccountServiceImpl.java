package com.kuaizhan.kzweixin.service.impl;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.config.ApplicationConfig;
import com.kuaizhan.kzweixin.config.WxApiConfig;
import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.constant.KzExchange;
import com.kuaizhan.kzweixin.dao.mapper.AccountDao;
import com.kuaizhan.kzweixin.dao.mapper.auto.AccountMapper;
import com.kuaizhan.kzweixin.cache.AccountCache;
import com.kuaizhan.kzweixin.entity.account.AccessTokenDTO;
import com.kuaizhan.kzweixin.exception.BusinessException;
import com.kuaizhan.kzweixin.exception.kuaizhan.KzApiException;
import com.kuaizhan.kzweixin.exception.weixin.WxIPNotInWhitelistException;
import com.kuaizhan.kzweixin.exception.weixin.WxInvalidAppSecretException;
import com.kuaizhan.kzweixin.manager.KzManager;
import com.kuaizhan.kzweixin.manager.WxAccountManager;
import com.kuaizhan.kzweixin.manager.WxThirdPartManager;
import com.kuaizhan.kzweixin.entity.account.AuthorizerInfoDTO;
import com.kuaizhan.kzweixin.dao.po.AccountPO;
import com.kuaizhan.kzweixin.entity.account.AuthorizationInfoDTO;
import com.kuaizhan.kzweixin.dao.po.auto.Account;
import com.kuaizhan.kzweixin.dao.po.auto.AccountExample;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.ThirdPartService;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import com.kuaizhan.kzweixin.utils.MqUtil;
import com.kuaizhan.kzweixin.utils.UrlUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by liangjiateng on 2017/3/15.
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountCache accountCache;
    @Resource
    private AccountDao accountDao;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private MqUtil mqUtil;
    @Resource
    private ThirdPartService thirdPartService;

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);


    @Override
    public String getBindUrl(Long userId, Long siteId) {
//        String redirectUrl = ApplicationConfig.KZ_DOMAIN_MAIN + "/weixin/account-bind-callback?userId=" + userId;
        String redirectUrl = "http://94dbabe2de.kzsite09.cn:8080/v1/accounts/tmp?userId=";
        redirectUrl += userId;
        // 同时绑定站点
        if (siteId != null) {
            redirectUrl += "&siteId=" + siteId;
        }
        redirectUrl = UrlUtil.encode(redirectUrl);
        String preAuthCode = WxThirdPartManager.getPreAuthCode(thirdPartService.getComponentAccessToken());

        return WxApiConfig.getBindUrl(preAuthCode, redirectUrl);
    }

    @Override
    public void bindAccount(Long userId, String authCode, Long siteId) {
        // 获取授权信息
        String componentAccessToken = thirdPartService.getComponentAccessToken();
        AuthorizationInfoDTO.Info authInfo = WxThirdPartManager.getAuthorizationInfo(
                ApplicationConfig.WEIXIN_APPID_THIRD,
                componentAccessToken, authCode)
                .getInfo();

        // 获取授权者信息
        String appId = authInfo.getAppId();
        AuthorizerInfoDTO.Info authorizerInfo = WxThirdPartManager.getAuthorizerInfo(
                ApplicationConfig.WEIXIN_APPID_THIRD,
                componentAccessToken,
                appId)
                .getInfo();

        // 是否有现存的
        AccountExample example = new AccountExample();
        example.createCriteria()
                .andAppIdEqualTo(appId)
                .andIsDelEqualTo(0);
        List<Account> results = accountMapper.selectByExample(example);

        Long weixinAppid;

        // 新用户
        if (results.size() == 0) {

            // 以前是否绑定过
            example = new AccountExample();
            example.createCriteria()
                    .andAppIdEqualTo(appId)
                    .andIsDelEqualTo(1);
            example.setOrderByClause("unbind_time DESC"); // 尽量取最新的历史记录
            List<Account> oldResults = accountMapper.selectByExample(example);

            // 绑定过，恢复老的数据
            if (oldResults.size() > 0) {

                // 老的记录
                Account record = oldResults.get(0);
                weixinAppid = record.getWeixinAppid();
                // 删除状态设为0
                record.setIsDel(0);
                record.setSiteId(siteId);
                record.setUserId(userId);

                // TODO: 清理accessToken
                setAccountRecord(record, authInfo, authorizerInfo);
                accountMapper.updateByPrimaryKeySelective(record);
            // 没绑定过，新增
            } else {
                Account record = new Account();
                weixinAppid = genWeixinAppid();

                record.setWeixinAppid(weixinAppid);
                record.setSiteId(siteId);
                record.setUserId(userId);

                record.setAppId(appId);
                // 后面改为由对象序列化
                record.setInterestJson("[\"0\",\"0\",\"0\",\"0\",\"0\",\"0\"]");
                record.setAdvancedFuncInfoJson("{\"open_login\":0,\"open_share\":0}");
                record.setMenuJson("");
                record.setCreateTime(DateUtil.curSeconds());

                setAccountRecord(record, authInfo, authorizerInfo);
                accountMapper.insertSelective(record);
            }
            // 各种导入的异步任务
            mqUtil.publish(KzExchange.USER_IMPORT, "", JsonUtil.bean2String(ImmutableMap.of("weixin_appid", weixinAppid)));

        // 老用户没有解绑，在某些场景下触发再次绑定, 更新绑定信息
        } else if (results.size() == 1){
            Account record = results.get(0);
            weixinAppid = record.getWeixinAppid();
            record.setUserId(userId);
            record.setSiteId(siteId);

            setAccountRecord(record, authInfo, authorizerInfo);
            accountMapper.updateByPrimaryKeySelective(record);
        } else {
            // 当前就绑定了两个，垃圾数据
            logger.error("[bindAccount:垃圾数据] appId当前有多个绑定, appId:" + appId);
            return;
        }

        // 清理之前可能缓存的账户信息和accessToken信息
        accountCache.deleteAccount(weixinAppid);
        accountCache.deleteAccessToken(weixinAppid);

        // 清理php缓存
        if (siteId != null) {
            accountCache.deletePhpAccountBySiteId(siteId);
        }
    }

    @Override
    public String getAccessToken(long weixinAppId) {
        //存在高并发场景下access_token失效的问题
        String accessToken = accountCache.getAccessToken(weixinAppId);
        // TODO: 在这里实现分布式锁，否则在消息队列和web同时调用的情况下，有一定几率相互覆盖

        if (accessToken == null) {
            AccountPO accountPO = getAccountByWeixinAppId(weixinAppId);
            //刷新
            AccessTokenDTO accessTokenDTO = WxAccountManager.refreshAccessToken(
                    ApplicationConfig.WEIXIN_APPID_THIRD,
                    thirdPartService.getComponentAccessToken(),
                    accountPO.getAppId(),
                    accountPO.getRefreshToken());
            accessToken = accessTokenDTO.getAccessToken();

            // 更新数据库
            Account record = new Account();
            record.setWeixinAppid(accountPO.getWeixinAppId());
            record.setRefreshToken(accessTokenDTO.getRefreshToken());
            // 数据库中对accessToken和expireTime字段的维护，应该是没有必要的
            record.setAccessToken(accessTokenDTO.getAccessToken());
            record.setExpiresTime(accessTokenDTO.getExpiresIn() + DateUtil.curSeconds() - 10 * 60);
            accountMapper.updateByPrimaryKeySelective(record);

            //设置缓存
            accountCache.setAccessToken(weixinAppId, accessTokenDTO);
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
        AccountPO accountPO = accountCache.getAccount(weinxinAppid);

        if (accountPO == null) {
            //从数据库拿
            accountPO = accountDao.getAccountByWeixinAppId(weinxinAppid);
            if (accountPO == null) {
                throw new BusinessException(ErrorCode.ACCOUNT_NOT_EXIST);
            }

            //存缓存
            accountCache.setAccount(accountPO);
        }
        return accountPO;
    }

    @Override
    public void updateAppSecret(long weixinAppId, String appSecret) {
        AccountPO accountPO = getAccountByWeixinAppId(weixinAppId);

        //调用WxAccountManager，把用户ID和app_secret发给微信服务器接口，获取access token并验证
        try {
            WxAccountManager.getUserAccessToken(accountPO.getAppId(), appSecret);
        } catch (WxIPNotInWhitelistException e) {
            throw new BusinessException(ErrorCode.IP_NOT_IN_WHITELIST);
        } catch (WxInvalidAppSecretException e) {
            throw new BusinessException(ErrorCode.INVALID_APP_SECRET);
        }

        AccountPO updatePO = new AccountPO();
        updatePO.setWeixinAppId(weixinAppId);
        updatePO.setAppSecret(appSecret);
        accountDao.updateAccountByWeixinAppId(updatePO);

        // 清理缓存
        accountCache.deleteAccount(weixinAppId);
    }

    @Override
    public void updateCustomizeShare(long weixinAppId, Integer openShare) {
        AccountPO accountPO = getAccountByWeixinAppId(weixinAppId);
        JSONObject jsonObject;

        //检测数据库是否存在记录，并更新自定义分享状态
        if ("".equals(accountPO.getAdvancedFuncInfoJson())) {
            jsonObject = new JSONObject();
        } else {
            jsonObject = new JSONObject(accountPO.getAdvancedFuncInfoJson());
        }
        jsonObject.put("open_share", openShare);

        AccountPO updatePO = new AccountPO();
        updatePO.setWeixinAppId(weixinAppId);
        updatePO.setAdvancedFuncInfoJson(jsonObject.toString());
        accountDao.updateAccountByWeixinAppId(updatePO);

        // 清理缓存
        accountCache.deleteAccount(weixinAppId);
    }

    @Override
    public void updateAuthLogin(long weixinAppId, Integer openLogin) {
        AccountPO accountPO = getAccountByWeixinAppId(weixinAppId);
        JSONObject jsonObject;

        //检测数据库是否存在记录
        if ("".equals(accountPO.getAdvancedFuncInfoJson())) {
            jsonObject = new JSONObject();
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

        // 清理缓存
        accountCache.deleteAccount(weixinAppId);
    }

    private void setAccountRecord(Account record,
                                  AuthorizationInfoDTO.Info authInfo,
                                  AuthorizerInfoDTO.Info authorizerInfo) {

        // 授权信息
        record.setAccessToken(authInfo.getAccessToken());
        record.setRefreshToken(authInfo.getRefreshToken());
        record.setExpiresTime(authInfo.getExpiresIn() + DateUtil.curSeconds() - 10 * 60);
        record.setFuncInfoJson(JsonUtil.list2Str(authInfo.getFuncInfoList()));

        // 授权者信息
        record.setNickName(authorizerInfo.getNickName());
        record.setHeadImg(authorizerInfo.getHeadImg());
        record.setServiceType(authorizerInfo.getServiceTypeInfo().getId());
        record.setVerifyType(authorizerInfo.getVerifyTypeInfo().getId());
        record.setUserName(authorizerInfo.getUsername());
        record.setAlias(authorizerInfo.getAlias());
        record.setBusinessInfoJson(JsonUtil.bean2String(authorizerInfo.getBusinessInfo()));
        record.setQrcodeUrl(authorizerInfo.getQrcodeUrl());

        record.setUpdateTime(DateUtil.curSeconds());
        record.setBindTime(DateUtil.curSeconds());
    }

    /**
     * 生成service
     */
    private long genWeixinAppid() {

        final long MIN = 1000000000L;
        final long MAX = 9999999999L;
        int count = 1;

        long weixinAppid = ThreadLocalRandom.current().nextLong(MIN, MAX + 1);
        while (accountMapper.selectByPrimaryKey(weixinAppid) != null) {
            weixinAppid = ThreadLocalRandom.current().nextLong(MIN, MAX + 1);
            count++;
        }
        // 随机三次及以上才随机到，报警
        if (count >= 3) {
            logger.warn("[genWeixinAppid] gen times reach {}", count);
        }
        return weixinAppid;
    }
}
