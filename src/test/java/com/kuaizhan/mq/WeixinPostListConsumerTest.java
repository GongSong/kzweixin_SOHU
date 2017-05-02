package com.kuaizhan.mq;

import com.kuaizhan.pojo.DTO.WxPostListDTO;
import com.kuaizhan.service.PostService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lorin on 17-4-1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class WeixinPostListConsumerTest {

    @Resource
    PostService postService;

    public static final long WEIXIN_APPID = 601145633L;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void onMessage() throws Exception {
        List<WxPostListDTO.PostItem> postItemList = postService.listNonExistsPostItemsFromWeixin(WEIXIN_APPID);
        for (WxPostListDTO.PostItem postItem: postItemList) {
            System.out.println("----->" + postItem.getItem().getMediaId());
        }
    }

}