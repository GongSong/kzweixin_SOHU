package com.kuaizhan.dao.mongo;

import com.kuaizhan.pojo.DO.ArticleDO;
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
    public void getArticleById() throws Exception {
    }

    @Test
    public void updateArticle() throws Exception {
    }

    @Test
    public void deleteArticle() throws Exception {
    }

    @Test
    public void insertArticle() throws Exception {
    }

    @Test
    public void listArticles() throws Exception {
    }

}