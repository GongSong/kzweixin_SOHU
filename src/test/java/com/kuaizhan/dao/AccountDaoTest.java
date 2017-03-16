package com.kuaizhan.dao;

import com.kuaizhan.dao.mapper.AccountDao;
import com.kuaizhan.pojo.DO.AccountDO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by liangjiateng on 2017/3/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class AccountDaoTest {

    @Resource
    AccountDao accountDao;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getAccountBySiteId() throws Exception {
        System.out.println("------>" + accountDao.getAccountBySiteId(123456L));
    }

    @Test
    public void getAccountByAppId() throws Exception {
        System.out.println("------>" + accountDao.getAccountByAppId("wx1a4ff9ec0e369bd1"));
    }

    @Test
    public void getDeleteAccountBySiteId() throws Exception {
        System.out.println("------>" + accountDao.getDeleteAccountBySiteId(123456L));
    }

    @Test
    public void insertAccount() throws Exception {
        AccountDO accountDO = accountDao.getAccountBySiteId(123456L);
        accountDO.setWeixinAppId(8888888888L);
        accountDO.setSiteId(1234567L);
        accountDao.insertAccount(accountDO);
    }

    @Test
    public void updateAccountBySiteId() throws Exception {
        AccountDO accountDO = accountDao.getAccountBySiteId(1234567L);
        accountDO.setNickName("fuck you");
        accountDao.updateAccountBySiteId(accountDO);
    }

    @Test
    public void deleteAccountByAppId() throws Exception {
        accountDao.deleteAccountByAppId("wx1a4ff9ec0e369bd1");
    }

}