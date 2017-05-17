package com.kuaizhan.service;

import com.kuaizhan.dao.mapper.MsgDao;
import com.kuaizhan.dao.redis.RedisMsgDao;
import com.kuaizhan.pojo.po.AccountPO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
    RedisMsgDao redisMsgDao;
    @Resource
    MsgDao msgDao;



}