package com.kuaizhan.service;

import com.kuaizhan.pojo.DTO.PostDTO;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

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
        weixinPostService.uploadImage(accountService.getAccountByWeixinAppId(601145633L).getAccessToken(),"http://192.168.110.218/g1/M00/01/27/CgoYr1YnRIWAUvk9AAAuGB6TobA1781741");
    }

    @Test
    public void listPostsByOffset() throws Exception {
        PostDTO postDTO = weixinPostService.listPostsByOffset(accountService.getAccountByWeixinAppId(601145633L).getAccessToken(), 0, 20);
        System.out.println("----->" + postDTO.toString());
    }

    @Test
    public void listAllPosts() throws Exception {
        List<PostDTO> postDTOList = weixinPostService.listAllPosts(accountService.getAccountByWeixinAppId(601145633L).getAccessToken());
        System.out.println("----->" + postDTOList.size());
    }
}