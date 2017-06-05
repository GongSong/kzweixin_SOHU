package com.kuaizhan.common;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.MenuService;
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
    private AccountService accountService;
    @Resource
    private MenuService menuService;

    @Test
    public void getAccessToken() throws Exception {
        String accessToken = accountService.getAccessToken(ApplicationConfig.WEIXIN_TEST_WEIXIN_APPID);
        String openId = "oBGGJt5SLU9P_wGu71Xo82m_Zq1s";
        System.out.println("---->" + accessToken);

    }

    @Test
    public void tmp() throws Exception {

    }
}
