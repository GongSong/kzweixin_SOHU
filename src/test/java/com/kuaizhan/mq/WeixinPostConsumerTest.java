package com.kuaizhan.mq;

import com.kuaizhan.utils.MqUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by lorin on 17-3-31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class WeixinPostConsumerTest {

    @Resource
    MqUtil mqUtil;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void onMessage() throws Exception {
//        WxPostListDTO.PostItem postItem = new WxPostListDTO.PostItem();
//        WxPostListDTO.Item item = new WxPostListDTO.Item();
//        WxPostListDTO.Item.Content content = new WxPostListDTO.Item.Content();
//        item.setContent(content);
//        item.setMediaId("asdfasdf");
//        item.setUpdateTime("DFASDF");
//        postItem.setItem(item);
//        postItem.setWeixinAppid(1123123);
//        Map<String, Object> message = new HashMap<>();
//        message.put("userId", 123456L);
//        String postItemJson = JsonUtil.bean2String(postItem);
//        message.put("postItem", postItemJson);
//        mqUtil.publish(MqConstant.IMPORT_WEIXIN_POST, message);
    }

}