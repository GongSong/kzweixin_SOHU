package com.kuaizhan.service;

import com.kuaizhan.dao.mapper.AccountDao;
import com.kuaizhan.pojo.po.AccountPO;
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
public class AccountServiceTest {



    @Resource
    AccountService accountService;
    @Resource
    AccountDao accountDao;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getAccountByWeixinAppId() throws Exception {
        System.out.println("---->"+accountService.getAccountByWeixinAppId(601145633L));
    }


    @Test
    public void bindAccount() throws Exception {
        AccountPO accountPO = accountDao.getAccountBySiteId(123456L);
        accountService.bindAccount(accountPO);
    }

    @Test
    public void getAccountBySiteId() throws Exception {
        System.out.println(accountService.getAccountBySiteId(12345L));
    }
    @Test
    public void unbindAccount() throws Exception {

    }

}