package com.kuaizhan.dao.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by zixiong on 2017/4/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class PostDaoTest {

    @Resource
    PostDao postDao;

    @Test
    public void deletePostReal() throws Exception {
    }

    @Test
    public void exists() {
        System.out.println("---->" +  1);
        System.out.println("---->" + postDao.exist(9616507302L, "x_L_iJd0_WYtc9HUX4QILyfIWnPc2TE3CtSE2oBuR4k"));
    }

}