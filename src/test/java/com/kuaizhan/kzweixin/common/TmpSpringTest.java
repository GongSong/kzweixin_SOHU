package com.kuaizhan.kzweixin.common;

import com.google.common.collect.ImmutableList;
import com.kuaizhan.kzweixin.config.ApplicationConfig;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.MenuService;
import com.kuaizhan.kzweixin.service.PostService;
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
    @Resource
    private PostService postService;

    @Test
    public void getAccessToken() throws Exception {
        String accessToken = accountService.getAccessToken(ApplicationConfig.WEIXIN_TEST_WEIXIN_APPID);
        String openId = "oBGGJt5SLU9P_wGu71Xo82m_Zq1s";
        System.out.println("---->" + accessToken);

    }

    @Test
    public void tmp() throws Exception {
        postService.importKzArticle(132413434L, ImmutableList.of(1323L, 32342L));
        postService.importKzArticle(132413434L, ImmutableList.of(1323L, 32342L));
        postService.importKzArticle(132413434L, ImmutableList.of(1323L, 32342L));
    }
}
