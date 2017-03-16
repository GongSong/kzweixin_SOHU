package com.kuaizhan.dao.mapper;

import com.kuaizhan.pojo.DO.UnbindDO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

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
        UnbindDO unbindDO=unbindDao.getUnbindByWeixinAppId(7122726025L);
        unbindDO.setWeixinAppId(1234567895L);
        unbindDao.insertUnbind(unbindDO);
    }

    @Test
    public void getUnbindByWeixinAppId() throws Exception {
        System.out.println("----->"+unbindDao.getUnbindByWeixinAppId(7122726025L));
    }

    @Test
    public void updateUnbindByWeixinAppId() throws Exception {
        UnbindDO unbindDO=unbindDao.getUnbindByWeixinAppId(7122726025L);
        unbindDO.setStatus(0);
        unbindDao.updateUnbindByWeixinAppId(unbindDO);
    }

}