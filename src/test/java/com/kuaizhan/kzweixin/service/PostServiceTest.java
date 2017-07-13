package com.kuaizhan.kzweixin.service;


import com.kuaizhan.kzweixin.dao.po.PostPO;
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
    private PostService postService;

    @Test
    public void export2KzArticle() throws Exception {
        postService.export2KzArticle(1900333742L,3911574514L,123456L);
    }

    @Test
    public void insertMultiPosts() throws Exception {
        long weixinAppid = 1789089804L;
        List<PostPO> posts = new ArrayList<>();

        String content = "<img onclick=\"clickcallback\" something />前面<script href=\"haha\"> fdaefdada </script>中间<script href=\"haha\"> fdaefdada </script>后面<img onclick=\"clickcallback\" something />";
        PostPO postPO = new PostPO();
        postPO.setContent(content);
        postPO.setTitle("新建图文单元测试");
        postPO.setDigest("我是摘要");
        postPO.setContentSourceUrl("www.baidu.com");
        postPO.setShowCoverPic((short) 1);
        postPO.setThumbUrl("http://mmbiz.qpic.cn/mmbiz_jpg/UqZMrMwVpn3NhPSO38HJticpDwBv0du7Hpkia5icBVpNr8mwlzX01Y8hZUccYxmEHGRZBRA0XSIwj1ggJka8K6Jeg/0?wx_fmt=jpeg");

        posts.add(postPO);
        postService.addMultiPosts(weixinAppid, posts);
    }
}