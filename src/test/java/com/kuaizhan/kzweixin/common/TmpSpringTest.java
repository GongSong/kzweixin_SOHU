package com.kuaizhan.kzweixin.common;

import com.kuaizhan.kzweixin.config.ApplicationConfig;
import com.kuaizhan.kzweixin.dao.po.auto.AccountPO;
import com.kuaizhan.kzweixin.entity.XmlData;
import com.kuaizhan.kzweixin.enums.WxAuthority;
import com.kuaizhan.kzweixin.exception.common.XMLParseException;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.WxPushService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import java.util.Arrays;


/**
 * Created by zixiong on 2017/4/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class TmpSpringTest {

    @Resource
    private AccountService accountService;
    @Resource
    private WxPushService wxPushService;
    @Resource
    ApplicationContext applicationContext;

    @Test
    public void getAccessToken() throws Exception {
        String accessToken = accountService.getAccessToken(ApplicationConfig.WEIXIN_TEST_WEIXIN_APPID);

    }

    @Test
    public void getBeans() throws Exception {
        System.out.println("---->" + "##########################################################");
        System.out.println("---->" + Arrays.asList(applicationContext.getBeanDefinitionNames()));
        System.out.println("---->" + "##########################################################");
    }
}
