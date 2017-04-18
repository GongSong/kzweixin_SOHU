package com.kuaizhan.service;


import com.kuaizhan.config.ApiConfig;
import com.kuaizhan.pojo.DTO.ArticleDTO;

import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.PostDTO;
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
 * Created by lorin on 17-3-30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class PostServiceTest {


    @Resource
    PostService postService;

    long weixinAppid = 601145633L;

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
        System.out.println("----->"+ ApiConfig.kzArticleUrl(3337333798L));
//        ArticleDTO articleDTO = postService.getKzArticle(3337333798L);
//        System.out.println("------>" + articleDTO.toString());
    }

    @Test
    public void export2KzArticle() throws Exception {
        postService.export2KzArticle(1900333742L,3911574514L,123456L);
    }

    @Test
    public void syncWeixinPosts() throws Exception {
        long weixinAppid = 1789089804L;
        long uid = 123L;
        postService.syncWeixinPosts(weixinAppid, uid);
    }

    @Test
    public void insertMultiPosts() throws Exception {
        long weixinAppid = 1789089804L;
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

    @Test
    public void listPostsByWeixinAppid() throws Exception {

    }

    @Test
    public void listMediaIdsByWeixinAppid() throws Exception {
        List<String> mediaIds = postService.listMediaIdsByWeixinAppid(weixinAppid);
        for (String mediaId: mediaIds) {
            System.out.println("----->" + mediaId);
        }
    }

    @Test
    public void exist() throws Exception {
        System.out.println("---->" + postService.exist(9616507302L, "x_L_iJd0_WYtc9HUX4QILyfIWnPc2TE3CtSE2oBuR4k"));

    }

    @Test
    public void listNonExistsPostItemsFromWeixin() throws Exception {
        long weixinAppid = 1789089804L;
        List<PostDTO.PostItem> postItemList = postService.listNonExistsPostItemsFromWeixin(weixinAppid);
        for (PostDTO.PostItem postItem: postItemList){
            System.out.println("------>" + postItem);
        }
    }
}