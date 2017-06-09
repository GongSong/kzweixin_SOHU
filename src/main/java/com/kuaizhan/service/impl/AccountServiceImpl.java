package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.config.WxApiConfig;
import com.kuaizhan.constant.ErrorCode;
import com.kuaizhan.dao.mapper.AccountDao;
import com.kuaizhan.dao.mapper.UnbindDao;
import com.kuaizhan.dao.mapper.auto.SiteWeixinMapper;
import com.kuaizhan.dao.redis.RedisAccountDao;
import com.kuaizhan.exception.BusinessException;
import com.kuaizhan.exception.common.DaoException;
import com.kuaizhan.exception.common.RedisException;
import com.kuaizhan.exception.weixin.WxIPNotInWhitelistException;
import com.kuaizhan.exception.weixin.WxInvalidAppSecretException;
import com.kuaizhan.manager.WxAccountManager;
import com.kuaizhan.manager.WxAuthManager;
import com.kuaizhan.pojo.dto.AuthorizerInfoDTO;
import com.kuaizhan.pojo.po.AccountPO;
import com.kuaizhan.pojo.po.UnbindPO;
import com.kuaizhan.pojo.dto.AuthorizationInfoDTO;
import com.kuaizhan.pojo.po.auto.SiteWeixin;
import com.kuaizhan.pojo.po.auto.SiteWeixinExample;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.WeixinAuthService;
import com.kuaizhan.utils.DateUtil;
import com.kuaizhan.utils.UrlUtil;
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
    private RedisAccountDao redisAccountDao;
    @Resource
    private AccountDao accountDao;
    @Resource
    private UnbindDao unbindDao;
    @Resource
    private SiteWeixinMapper siteWeixinMapper;
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
    public void bindAccount(Long userId, String authCode, Long siteId) {
        String componentAccessToken = weixinAuthService.getComponentAccessToken();
        AuthorizationInfoDTO authorizationInfo = WxAuthManager.getAuthorizationInfo(ApplicationConfig.WEIXIN_APPID_THIRD, componentAccessToken, authCode);
        String appId = authorizationInfo.getAppId();
        AuthorizerInfoDTO authorizerInfo = WxAuthManager.getAuthorizerInfo(ApplicationConfig.WEIXIN_APPID_THIRD, componentAccessToken, appId);

        // 是否有现存的
        SiteWeixinExample example = new SiteWeixinExample();
        example.createCriteria()
                .andAppIdEqualTo(appId)
                .andIsDelEqualTo(0);
        List<SiteWeixin> results = siteWeixinMapper.selectByExample(example);

        // 新用户
        if (results.size() == 0) {
            // 以前是否绑定过
            example = new SiteWeixinExample();
            example.createCriteria()
                    .andAppIdEqualTo(appId)
                    .andIsDelEqualTo(1);
            example.setOrderByClause("unbind_time DESC"); // 尽量取最新的历史记录
            List<SiteWeixin> oldResults = siteWeixinMapper.selectByExample(example);

            // 更新老的数据
            if (oldResults.size() > 0) {

                // 老的记录
                SiteWeixin record = oldResults.get(0);
                // 删除状态设为0
                record.setIsDel(0);
                record.setSiteId(siteId);
                record.setUserId(userId);

                // TODO: 清理accessToken
                record.setAccessToken(authorizationInfo.getAccessToken());
                record.setRefreshToken(authorizationInfo.getRefreshToken());
                record.setExpiresTime(authorizationInfo.getExpiresIn() + DateUtil.curSeconds() - 10 * 60);
                record.setFuncInfoJson(authorizationInfo.getFuncInfo());

                record.setBindTime(DateUtil.curSeconds());
                record.setUpdateTime(DateUtil.curSeconds());

                siteWeixinMapper.updateByPrimaryKeySelective(record);
            // 不存在，新增
            } else {
                SiteWeixin record = new SiteWeixin();

                record.setWeixinAppid(genWeixinAppid());
                record.setSiteId(siteId);
                record.setUserId(userId);

                record.setAccessToken(authorizationInfo.getAccessToken());
                record.setRefreshToken(authorizationInfo.getRefreshToken());
                record.setExpiresTime(authorizationInfo.getExpiresIn() + DateUtil.curSeconds() - 10 * 60);
                record.setFuncInfoJson(authorizationInfo.getFuncInfo());

                record.setAppId(appId);
                // 后面改为由对象序列化
                record.setInterestJson("[\"0\",\"0\",\"0\",\"0\",\"0\",\"0\"]");
                record.setAdvancedFuncInfoJson("{\"open_login\":0,\"open_share\":0}");
                record.setMenuJson("");

                record.setBindTime(DateUtil.curSeconds());
                record.setCreateTime(DateUtil.curSeconds());
                record.setUpdateTime(DateUtil.curSeconds());

                siteWeixinMapper.insertSelective(record);
            }
            // 各种导入的异步任务

        // 老用户没有解绑，在某些场景下触发再次绑定,
        } else if (results.size() == 1){
            SiteWeixin record = results.get(0);
            record.setUserId(userId);
            record.setSiteId(siteId);

            record.setAccessToken(authorizationInfo.getAccessToken());
            record.setRefreshToken(authorizationInfo.getRefreshToken());
            record.setExpiresTime(authorizationInfo.getExpiresIn() + DateUtil.curSeconds() - 10 * 60);
            record.setFuncInfoJson(authorizationInfo.getFuncInfo());

            record.setBindTime(DateUtil.curSeconds());
            record.setUpdateTime(DateUtil.curSeconds());

            siteWeixinMapper.updateByPrimaryKeySelective(record);
        } else {
            // 当前就绑定了两个，垃圾数据
            logger.error("[bindAccount:垃圾数据] appId当前有多个绑定, appId:" + appId);
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

    private long genWeixinAppid() {

        final long MIN = 1000000000L;
        final long MAX = 9999999999L;
        int count = 1;

        long weixinAppid = ThreadLocalRandom.current().nextLong(MIN, MAX + 1);
        while (siteWeixinMapper.selectByPrimaryKey(weixinAppid) != null) {
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
