package com.kuaizhan.service;

import com.kuaizhan.dao.mapper.MsgDao;
import com.kuaizhan.dao.redis.RedisMsgDao;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DO.MsgDO;
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
public class MsgServiceTest {

    @Resource
    MsgService msgService;
    @Resource
    AccountService accountService;
    @Resource
    RedisMsgDao redisMsgDao;
    @Resource
    MsgDao msgDao;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void countMsg() throws Exception {
        AccountDO accountDO = accountService.getAccountBySiteId(123456L);
        System.out.println("----->"+msgService.countMsg(accountDO.getAppId(),2,1,null,0));
    }

    @Test
    public void listMsgsByPagination() throws Exception {

        System.out.println("------>"+msgService.listMsgsByPagination(123456L,"wx1a4ff9ec0e369bd1",1,null,0));
    }

    @Test
    public void listNewMsgs() throws Exception {
        msgService.listNewMsgs("wx1a4ff9ec0e369bd1");
    }

    @Test
    public void listMsgsByOpenId() throws Exception {

    }

    @Test
    public void updateMsgsStatus() throws Exception {

    }

    @Test
    public void sendMsgByOpenId() throws Exception {

    }

}