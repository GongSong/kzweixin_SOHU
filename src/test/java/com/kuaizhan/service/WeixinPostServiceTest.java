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
        weixinPostService.deletePost("33l4-QjvaEM2tuPBQiDIvm0i4TD3gMbJyg75kvsFaxg",accountService.getAccountByWeixinAppId(601145633L).getAccessToken());
    }

    @Test
    public void uploadImage() throws Exception {
        weixinPostService.uploadImage(accountService.getAccountByWeixinAppId(weixinAppid).getAccessToken(),"http://192.168.110.218/g1/M00/01/27/CgoYr1YnRIWAUvk9AAAuGB6TobA1781741");
    }

    @Test
    public void getPostDTOByOffset() throws Exception {
        PostDTO postDTO = weixinPostService.getPostDTOByOffset(accountService.getAccountByWeixinAppId(601145633L).getAccessToken(), 0, 20);
        System.out.println("----->" + postDTO.toString());
    }

    @Test
    public void listAllPosts() throws Exception {
        long weixinAppid = 1789089804L;
        List<PostDTO> postDTOList = weixinPostService.listAllPosts(accountService.getAccountByWeixinAppId(weixinAppid).getAccessToken());
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