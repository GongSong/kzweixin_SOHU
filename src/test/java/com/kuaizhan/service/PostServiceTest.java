package com.kuaizhan.service;


import com.kuaizhan.pojo.DTO.ArticleDTO;

import com.kuaizhan.pojo.DO.PostDO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zixiong on 2017/3/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class PostServiceTest {


    @Resource
    PostService postService;




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



    @Test
    public void insertMultiPosts1() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void export2KzArticle() throws Exception {
        postService.export2KzArticle(601145633L,1136830965,123,123456L);
    }

    @Test
    public void insertMultiPosts() throws Exception {
        long weixinAppid = 601145633;
        List<PostDO> posts = new ArrayList<>();

        String content = "<img onclick=\"clickcallback\" something />前面<script href=\"haha\"> fdaefdada </script>中间<script href=\"haha\"> fdaefdada </script>后面<img onclick=\"clickcallback\" something />";
        PostDO postDO = new PostDO();
        postDO.setContent(content);
        postDO.setTitle("新建图文单元测试");
        postDO.setDigest("我是摘要");
        postDO.setContentSourceUrl("www.baidu.com");
        postDO.setShowCoverPic((short) 1);
        postDO.setThumbUrl("http://mmbiz.qpic.cn/mmbiz_jpg/UqZMrMwVpn3NhPSO38HJticpDwBv0du7Hpkia5icBVpNr8mwlzX01Y8hZUccYxmEHGRZBRA0XSIwj1ggJka8K6Jeg/0?wx_fmt=jpeg");

        posts.add(postDO);
        postService.insertMultiPosts(weixinAppid, posts);
    }
}