package com.kuaizhan.dao.mongo;

import com.kuaizhan.pojo.DO.ArticleDO;
import com.kuaizhan.pojo.DO.MongoPostDo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

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
        MongoPostDo mongoPostDo = new MongoPostDo();
        long id = 1111111111442L;
        mongoPostDo.setId(id);
        String content1 = "I am content";
        mongoPostDo.setContent(content1);
        mongoPostDao.upsertPost(mongoPostDo);

        assertEquals(mongoPostDao.getPostById(id).getContent(), content1);

        String content2 = "2222222222";
        mongoPostDo.setContent(content2);
        mongoPostDao.upsertPost(mongoPostDo);

        assertEquals(mongoPostDao.getPostById(id).getContent(), content2);
        System.out.println("##########" + content2);

        mongoPostDao.deletePost(id);

        assertEquals(mongoPostDao.getPostById(id), null);
    }

}