package com.kuaizhan.kzweixin.service.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.config.ApplicationConfig;
import com.kuaizhan.kzweixin.config.WxApiConfig;
import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.constant.KzExchange;
import com.kuaizhan.kzweixin.dao.mapper.auto.AccountMapper;
import com.kuaizhan.kzweixin.cache.AccountCache;
import com.kuaizhan.kzweixin.entity.account.AccessTokenDTO;
import com.kuaizhan.kzweixin.exception.BusinessException;
import com.kuaizhan.kzweixin.exception.kuaizhan.KZPicUploadException;
import com.kuaizhan.kzweixin.exception.kuaizhan.KzApiException;
import com.kuaizhan.kzweixin.exception.weixin.WxIPNotInWhitelistException;
import com.kuaizhan.kzweixin.exception.weixin.WxInvalidAppSecretException;
import com.kuaizhan.kzweixin.manager.KzManager;
import com.kuaizhan.kzweixin.manager.WxAccountManager;
import com.kuaizhan.kzweixin.manager.WxThirdPartManager;
import com.kuaizhan.kzweixin.entity.account.AuthorizerInfoDTO;
import com.kuaizhan.kzweixin.dao.po.auto.AccountPO;
import com.kuaizhan.kzweixin.entity.account.AuthorizationInfoDTO;
import com.kuaizhan.kzweixin.dao.po.auto.AccountPOExample;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.WxThirdPartService;
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
    private AccountMapper accountMapper;
    @Resource
    private MqUtil mqUtil;
    @Resource
    private WxThirdPartService wxThirdPartService;

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);


    @Override
    public String getBindUrl(Long userId, Long siteId, String redirectUrl) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("http://").append(ApplicationConfig.KZ_DOMAIN_OUT).append(":8080").append("/v1/account/bind_redirect");
        urlBuilder.append("?userId=").append(userId);
        urlBuilder.append("&redirectUrl=").append(UrlUtil.encode(redirectUrl));
        if (siteId != null) {
            urlBuilder.append("&siteId=").append(siteId);
        }
        String preAuthCode = WxThirdPartManager.getPreAuthCode(wxThirdPartService.getComponentAccessToken());
        return WxApiConfig.getBindUrl(preAuthCode, UrlUtil.encode(urlBuilder.toString()));
    }

    @Override
    public void bindAccount(Long userId, String authCode, Long siteId) {
        // 获取授权信息
        String componentAccessToken = wxThirdPartService.getComponentAccessToken();
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
        AccountPOExample example = new AccountPOExample();
        example.createCriteria()
                .andAppIdEqualTo(appId)
                .andIsDelEqualTo(0);
        List<AccountPO> results = accountMapper.selectByExample(example);

        Long weixinAppid;

        // 新用户
        if (results.size() == 0) {

            // 以前是否绑定过
            example = new AccountPOExample();
            example.createCriteria()
                    .andAppIdEqualTo(appId)
                    .andIsDelEqualTo(1);
            example.setOrderByClause("unbind_time DESC"); // 尽量取最新的历史记录
            List<AccountPO> oldResults = accountMapper.selectByExample(example);

            // 绑定过，恢复老的数据
            if (oldResults.size() > 0) {

                // 老的记录
                AccountPO record = oldResults.get(0);
                weixinAppid = record.getWeixinAppid();
                // 删除状态设为0
                record.setIsDel(0);
                record.setSiteId(siteId);
                record.setUserId(userId);

                setAccountRecord(record, authInfo, authorizerInfo);
                accountMapper.updateByPrimaryKeySelective(record);
            // 没绑定过，新增
            } else {
                AccountPO record = new AccountPO();
                weixinAppid = genWeixinAppid();

                record.setWeixinAppid(weixinAppid);
                record.setSiteId(siteId);
                record.setUserId(userId);

                record.setAppId(appId);
                record.setInterestJson(JsonUtil.list2Str(ImmutableList.of("0","0","0","0","0","0")));
                record.setAdvancedFuncInfoJson(JsonUtil.bean2String(ImmutableMap.of("open_login", 0, "open_share", 0)));
                record.setMenuJson("");
                record.setCreateTime(DateUtil.curSeconds());

                setAccountRecord(record, authInfo, authorizerInfo);
                accountMapper.insertSelective(record);
            }

        // 老用户没有解绑，在某些场景下触发再次绑定, 更新绑定信息
        } else if (results.size() == 1){
            AccountPO record = results.get(0);
            weixinAppid = record.getWeixinAppid();
            record.setUserId(userId);
            record.setSiteId(siteId);

            setAccountRecord(record, authInfo, authorizerInfo);
            accountMapper.updateByPrimaryKeySelective(record);
        } else {
            // 当前就绑定了两个，垃圾数据
            logger.error("[bindAccount:垃圾数据] appId当前有多个绑定, appId:{}", appId);
            return;
        }

        // 清理之前可能缓存的账户信息和accessToken信息
        accountCache.deleteAccountByWeixinAppid(weixinAppid);
        accountCache.deleteAccountByAppid(appId);
        accountCache.deleteAccessToken(weixinAppid);

        // 用户导入异步任务，使用php的
        mqUtil.publish(KzExchange.USER_IMPORT, "", JsonUtil.bean2String(ImmutableMap.of("weixin_appid", weixinAppid)));
        // 将用户的头像转为快站url

        // 清理php缓存, 完全重构后删除
        if (siteId != null) {
            accountCache.deletePhpAccountBySiteId(siteId);
        }
    }

    @Override
    public List<AccountPO> getAccounts(long userId) {
        AccountPOExample example = new AccountPOExample();
        example.createCriteria()
                .andUserIdEqualTo(userId)
                .andIsDelEqualTo(0);
        return accountMapper.selectByExample(example);
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
                    wxThirdPartService.getComponentAccessToken(),
                    accountPO.getAppId(),
                    accountPO.getRefreshToken());
            accessToken = accessTokenDTO.getAccessToken();

            // 更新数据库
            AccountPO record = new AccountPO();
            record.setWeixinAppid(accountPO.getWeixinAppid());
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
        AccountPOExample example = new AccountPOExample();
        example.createCriteria()
                .andSiteIdEqualTo(siteId)
                .andIsDelEqualTo(0);
        List<AccountPO> accountPOS = accountMapper.selectByExample(example);

        if (accountPOS.size() == 0) {
            throw new BusinessException(ErrorCode.SITE_ID_NOT_EXIST);
        }
        return accountPOS.get(0);
    }

    @Override
    public AccountPO getAccountByAppId(String appId) {
        AccountPO accountPO = accountCache.getAccountByAppid(appId);

        if (accountPO == null) {

            AccountPOExample example = new AccountPOExample();
            example.createCriteria()
                    .andAppIdEqualTo(appId)
                    .andIsDelEqualTo(0);
            List<AccountPO> accountPOS = accountMapper.selectByExample(example);

            if (accountPOS.size() == 0) {
                throw new BusinessException(ErrorCode.APP_ID_NOT_EXIST);
            }
            accountPO = accountPOS.get(0);
            accountCache.setAccountByAppid(accountPO);
        }
        return accountPO;
    }

    @Override
    public AccountPO getAccountByWeixinAppId(long weixinAppid) {
        // 从缓存拿
        AccountPO accountPO = accountCache.getAccountByWeixinAppid(weixinAppid);

        if (accountPO == null) {
            //从数据库拿
            accountPO = accountMapper.selectByPrimaryKey(weixinAppid);

            if (accountPO == null || accountPO.getIsDel() == 1) {
                throw new BusinessException(ErrorCode.ACCOUNT_NOT_EXIST);
            }
            //存缓存
            accountCache.setAccountByWeixinAppid(accountPO);
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

        AccountPO record = new AccountPO();
        record.setWeixinAppid(weixinAppId);
        record.setAppSecret(appSecret);
        accountMapper.updateByPrimaryKeySelective(record);

        // 清理缓存
        accountCache.deleteAccountByWeixinAppid(weixinAppId);
        accountCache.deleteAccountByAppid(accountPO.getAppId());
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

        AccountPO record = new AccountPO();
        record.setWeixinAppid(weixinAppId);
        record.setAdvancedFuncInfoJson(jsonObject.toString());
        accountMapper.updateByPrimaryKeySelective(record);

        // 清理缓存
        accountCache.deleteAccountByWeixinAppid(weixinAppId);
        accountCache.deleteAccountByAppid(accountPO.getAppId());
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

                AccountPO record = new AccountPO();
                record.setWeixinAppid(weixinAppId);
                record.setAdvancedFuncInfoJson(jsonObject.toString());
                accountMapper.updateByPrimaryKeySelective(record);

                throw new BusinessException(ErrorCode.NOT_SERVICE_NUMBER);
            }
            jsonObject.put("open_login", openLogin);
        }

        AccountPO record = new AccountPO();
        record.setWeixinAppid(weixinAppId);
        record.setAdvancedFuncInfoJson(jsonObject.toString());
        accountMapper.updateByPrimaryKeySelective(record);

        // 清理缓存
        accountCache.deleteAccountByWeixinAppid(weixinAppId);
        accountCache.deleteAccountByAppid(accountPO.getAppId());
    }

    @Override
    public void uploadQrcode2Kz(long weixinAppid) {
        AccountPO accountPO = getAccountByWeixinAppId(weixinAppid);
        String qrcodeUrlKz;
        try {
            qrcodeUrlKz = KzManager.uploadPicToKz(accountPO.getQrcodeUrl());
        } catch (KZPicUploadException e) {
            logger.error("[uploadQrcode2Kz] upload pic failed, weixinAppid:{}", weixinAppid);
            return;
        }

        AccountPO record = new AccountPO();
        record.setWeixinAppid(weixinAppid);
        record.setQrcodeUrlKz(qrcodeUrlKz);
        accountMapper.updateByPrimaryKeySelective(record);
    }

    private void setAccountRecord(AccountPO record,
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
