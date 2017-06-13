package com.kuaizhan.kzweixin.dao.mapper;

import com.kuaizhan.kzweixin.pojo.po.UnbindPO;
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
public class UnbindDaoTest {

    @Resource
    UnbindDao unbindDao;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void insertUnbind() throws Exception {
        UnbindPO unbindPO =unbindDao.getUnbindByWeixinAppId(7122726025L);
        unbindPO.setWeixinAppId(1234567895L);
        unbindDao.insertUnbind(unbindPO);
    }

    @Test
    public void getUnbindByWeixinAppId() throws Exception {
        System.out.println("----->"+unbindDao.getUnbindByWeixinAppId(7122726025L));
    }

    @Test
    public void updateUnbindByWeixinAppId() throws Exception {
        UnbindPO unbindPO =unbindDao.getUnbindByWeixinAppId(7122726025L);
        unbindPO.setStatus(0);
        unbindDao.updateUnbindByWeixinAppId(unbindPO);
    }

}