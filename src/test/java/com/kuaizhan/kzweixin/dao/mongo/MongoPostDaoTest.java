package com.kuaizhan.kzweixin.dao.mongo;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by liangjiateng on 2017/3/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class MongoPostDaoTest {
    @Resource
    MongoPostDao mongoPostDao;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void upsertPost() throws Exception {
        long pageId = 111;

        String content1 = "content1";
        mongoPostDao.upsertPost(pageId, content1);
        assertEquals(content1, mongoPostDao.getContentById(pageId));

        String content2 = "content2";
        mongoPostDao.upsertPost(pageId, content2);
        assertEquals(content2, mongoPostDao.getContentById(pageId));

        mongoPostDao.deletePost(pageId);
        assertEquals("", mongoPostDao.getContentById(pageId));

    }

}