package com.kuaizhan.service;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.redis.RedisAuthDao;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DTO.AuthorizationInfoDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

import static org.junit.Assert.*;

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
        AccountDO accountDO=accountService.getAccountBySiteId(123456L);
        System.out.println("----->" + weixinAuthService.refreshAuthorizationInfo(weixinAuthService.getComponentAccessToken(), ApplicationConfig.WEIXIN_APPID_THIRD, accountDO.getAppId(), accountDO.getRefreshToken()));
    }

    @Test
    public void getAuthorizerInfo() throws Exception {
        AuthorizationInfoDTO authorizationInfoDTO = weixinAuthService.getAuthorizationInfo(weixinAuthService.getComponentAccessToken(), ApplicationConfig.WEIXIN_APPID_THIRD, "queryauthcode@@@NDrTsyBqHI8zJJAVgF4LBXIvCjtgSeTDYNCASfx6DPDXJMBkQ9DshST0kulpVeIzmq775nrH1xz1Wx-I8sTCHQ");
        System.out.println("----->" + weixinAuthService.getAuthorizerInfo(weixinAuthService.getComponentAccessToken(), ApplicationConfig.WEIXIN_APPID_THIRD, authorizationInfoDTO.getAppId()));
    }

}