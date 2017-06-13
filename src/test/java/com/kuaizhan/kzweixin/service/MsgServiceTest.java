package com.kuaizhan.kzweixin.service;

import com.kuaizhan.kzweixin.dao.mapper.MsgDao;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by liangjiateng on 2017/3/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class MsgServiceTest {

    @Resource
    MsgService msgService;
    @Resource
    AccountService accountService;
    @Resource
    MsgDao msgDao;



}