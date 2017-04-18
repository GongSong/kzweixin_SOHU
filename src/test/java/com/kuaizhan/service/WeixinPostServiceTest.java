package com.kuaizhan.service;

import com.kuaizhan.pojo.DTO.PostDTO;

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
        PostDTO postDTO = weixinPostService.getPostDTOByOffset(accountService.getAccessToken(601145633L), 0, 20);
        System.out.println("----->" + postDTO.toString());
    }

    @Test
    public void listAllPosts() throws Exception {
        long weixinAppid = 1789089804L;
        List<PostDTO> postDTOList = weixinPostService.listAllPosts(accountService.getAccessToken(weixinAppid));
        List<PostDTO.PostItem> postItemList = new LinkedList<>();
        for (PostDTO postDTO: postDTOList) {
            postItemList.addAll(postDTO.toPostItemList(weixinAppid));
        }
        for (PostDTO.PostItem postItem :
                postItemList) {
            System.out.println("----->" + postItem.toString());
        }
    }
}