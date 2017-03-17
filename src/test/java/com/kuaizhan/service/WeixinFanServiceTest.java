package com.kuaizhan.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by liangjiateng on 2017/3/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class WeixinFanServiceTest {
    @Resource
    WeixinFanService weixinFanService;
    @Resource
    AccountService accountService;
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void listTags() throws Exception {

    }

    @Test
    public void insertTag() throws Exception {
        weixinFanService.insertTag(accountService.getAccountBySiteId(123456L).getAccessToken(),"sssss");
    }

    @Test
    public void updateTag() throws Exception {

    }

    @Test
    public void deleteTag() throws Exception {

    }

    @Test
    public void renameTag() throws Exception {

    }

    @Test
    public void insertBlack() throws Exception {

    }

    @Test
    public void removeBlack() throws Exception {

    }

}