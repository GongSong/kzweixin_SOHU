package com.kuaizhan.kzweixin.service.impl;

import org.junit.Test;

import javax.annotation.Resource;
import com.kuaizhan.kzweixin.service.FanService;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by fangtianyu on 6/29/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class FanServiceImplTest {
    @Resource
    private FanService fanService;

    @Test
    public void userSubscribe() throws Exception {
        String appId = "wx118d7fb9ab2c9f8a";
        String openId = "o_0ZuuAFUWo1JEv1pWdg7ak12345";
        fanService.userSubscribe(appId, openId);
    }

    @Test
    public void userUnsubscribe() throws Exception {
        String appId = "wx118d7fb9ab2c9f8a";
        String openId = "o_0ZuuONsN8NFfGG-4CJ8xe5IqKE";
        fanService.userUnsubscribe(appId, openId);
    }

    @Test
    public void refreshUserInfo() throws Exception {
        long weixinAppid = 8111772986L;
        String appId = "wx1a4ff9ec0e369bd1";
        String openId = "oBGGJt--64-vu1E6AjHDZ0pEIw9E";
        fanService.refreshUserInfo(weixinAppid, appId, openId, false);
    }

}