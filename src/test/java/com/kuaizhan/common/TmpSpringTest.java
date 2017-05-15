package com.kuaizhan.common;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


/**
 * Created by zixiong on 2017/4/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class TmpSpringTest {

    @Resource
    AccountService accountService;

    @Test
    public void getAccessToken() throws Exception {
        System.out.println("---->" + accountService.getAccessToken(ApplicationConfig.WEIXIN_TEST_WEIXIN_APPID));
    }
}
