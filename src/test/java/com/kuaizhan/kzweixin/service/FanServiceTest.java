package com.kuaizhan.kzweixin.service;

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
public class FanServiceTest {

    @Resource
    FanService fanService;
    @Resource
    AccountService accountService;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

//    @Test
//    public void countFan() throws Exception {
//        System.out.println("----->" + fanService.countFan("wx1a4ff9ec0e369bd1", 0, null, null));
//    }
//
//    @Test
//    public void listFanByPagination() throws Exception {
//        System.out.println("----->" + fanService.listFanByPagination(123456L, "wx1a4ff9ec0e369bd1", 1, 0, null, null));
//    }

    @Test
    public void listTag() throws Exception {

    }

    @Test
    public void insertTag() throws Exception {

    }

    @Test
    public void updateUserTag() throws Exception {

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
    public void deleteBlack() throws Exception {

    }

}