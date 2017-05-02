package com.kuaizhan.service;

import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.WxPostListDTO;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by liangjiateng on 2017/3/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class WeixinPostServiceTest {

    @Resource
    WeixinPostService weixinPostService;
    @Resource
    AccountService accountService;

    long weixinAppid = 601145633L;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void deletePost() throws Exception {
        weixinPostService.deletePost("33l4-QjvaEM2tuPBQiDIvm0i4TD3gMbJyg75kvsFaxg",accountService.getAccessToken(601145633L));
    }

    @Test
    public void uploadImageForPost() throws Exception{
        weixinPostService.uploadImgForPost(accountService.getAccessToken(2326841584L), "http://pic.kuaizhan.com/g1/M01/37/F3/CgpQU1YawjyABj6HAATcqHQantk1904826/imageView/v1/thumbnail/240x180");
    }

    @Test
    public void uploadImage() throws Exception {
        weixinPostService.uploadImage(accountService.getAccessToken(weixinAppid),"http://192.168.110.218/g1/M00/01/27/CgoYr1YnRIWAUvk9AAAuGB6TobA1781741");
    }

    @Test
    public void getPostDTOByOffset() throws Exception {
        WxPostListDTO wxPostListDTO = weixinPostService.getWxPostList(accountService.getAccessToken(601145633L), 0, 20);
        System.out.println("----->" + wxPostListDTO.toString());
    }

    @Test
    public void updatePosts() throws Exception {
        long weixinAppid = 2326841584L;
        PostDO postDO = new PostDO();
        postDO.setTitle("买房故事被修改");
        postDO.setContent("我是个简单的内容");
        postDO.setThumbMediaId("vleui19nacBl3_Of7NdcuEOXTNprXPU0do2qWNbLaps");
        weixinPostService.updatePost(accountService.getAccessToken(weixinAppid),"vleui19nacBl3_Of7NdcuFpwLlYwyh7BafqUY9qGoMU", postDO);
    }

}