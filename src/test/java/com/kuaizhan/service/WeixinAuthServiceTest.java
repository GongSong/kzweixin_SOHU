package com.kuaizhan.service;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.redis.RedisAuthDao;
import com.kuaizhan.pojo.po.AccountPO;
import com.kuaizhan.pojo.dto.AuthorizationInfoDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by liangjiateng on 2017/3/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class WeixinAuthServiceTest {

    @Resource
    WeixinAuthService weixinAuthService;
    @Resource
    RedisAuthDao redisAuthDao;
    @Resource
    AccountService accountService;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void checkMsg() throws Exception {

    }

    @Test
    public void getComponentVerifyTicket() throws Exception {
        System.out.println("----->" + redisAuthDao.getComponentVerifyTicket());
    }

    @Test
    public void getComponentAccessToken() throws Exception {
        System.out.println("----->" + weixinAuthService.getComponentAccessToken());
    }

    @Test
    public void getPreAuthCode() throws Exception {
        System.out.println("----->" + weixinAuthService.getPreAuthCode(weixinAuthService.getComponentAccessToken()));
    }

    @Test
    public void getAuthorizationInfo() throws Exception {
        System.out.println("----->" + weixinAuthService.getAuthorizationInfo(weixinAuthService.getComponentAccessToken(), ApplicationConfig.WEIXIN_APPID_THIRD, "queryauthcode@@@NDrTsyBqHI8zJJAVgF4LBXIvCjtgSeTDYNCASfx6DPDXJMBkQ9DshST0kulpVeIzmq775nrH1xz1Wx-I8sTCHQ"));
    }

    @Test
    public void refreshAuthorizationInfo() throws Exception {
        AccountPO accountPO = accountService.getAccountBySiteId(123456L);
        System.out.println("----->" + weixinAuthService.refreshAuthorizationInfo(weixinAuthService.getComponentAccessToken(), ApplicationConfig.WEIXIN_APPID_THIRD, accountPO.getAppId(), accountPO.getRefreshToken()));
    }

    @Test
    public void getAuthorizerInfo() throws Exception {
        AuthorizationInfoDTO authorizationInfoDTO = weixinAuthService.getAuthorizationInfo(weixinAuthService.getComponentAccessToken(), ApplicationConfig.WEIXIN_APPID_THIRD, "queryauthcode@@@NDrTsyBqHI8zJJAVgF4LBXIvCjtgSeTDYNCASfx6DPDXJMBkQ9DshST0kulpVeIzmq775nrH1xz1Wx-I8sTCHQ");
        System.out.println("----->" + weixinAuthService.getAuthorizerInfo(weixinAuthService.getComponentAccessToken(), ApplicationConfig.WEIXIN_APPID_THIRD, authorizationInfoDTO.getAppId()));
    }

}