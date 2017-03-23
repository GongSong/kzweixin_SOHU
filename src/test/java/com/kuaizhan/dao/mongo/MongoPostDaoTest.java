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
        ArticleDO articleDO = mongoPostDao.getArticleById(1L);
        System.out.println("----->" + articleDO);
    }

    @Test
    public void updateArticle() throws Exception {
        ArticleDO articleDO = mongoPostDao.getArticleById(2L);
        System.out.println("----->" + articleDO);
        articleDO.setContent("fuck");
        mongoPostDao.updateArticle(articleDO);
        ArticleDO articleDO1 = mongoPostDao.getArticleById(2L);
        System.out.println("----->" + articleDO1);
    }

    @Test
    public void deleteArticle() throws Exception {
        mongoPostDao.deleteArticle(1L);
    }

    @Test
    public void insertArticle() throws Exception {
        ArticleDO articleDO = new ArticleDO();
        articleDO.setId(6L);
        articleDO.setTitle("test");
        articleDO.setContent("ddd");
        mongoPostDao.insertArticle(articleDO);
    }

    @Test
    public void listArticles() throws Exception {
        System.out.println("------>"+mongoPostDao.listArticles());
    }

}