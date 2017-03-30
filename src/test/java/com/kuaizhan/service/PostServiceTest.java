package com.kuaizhan.service;

import com.kuaizhan.pojo.DTO.ArticleDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by liangjiateng on 2017/3/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class PostServiceTest {

    @Resource
    PostService postService;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void listPostsByPagination() throws Exception {

    }

    @Test
    public void listMultiPosts() throws Exception {

    }

    @Test
    public void deletePost() throws Exception {

    }

    @Test
    public void getPostByPageId() throws Exception {

    }

    @Test
    public void getKzArticle() throws Exception {
        ArticleDTO articleDTO = postService.getKzArticle(2745017759L);
        System.out.println("------>" + articleDTO.toString());
    }

}