package com.kuaizhan.service;


import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.WxPostListDTO;
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

    @Test
    public void genPageId() {
        System.out.println("---->" + postService.genPageId());
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
    public void calSyncWeixinPosts() throws Exception {
//        postService.calSyncWeixinPosts(2326841584L, 1);
    }
}