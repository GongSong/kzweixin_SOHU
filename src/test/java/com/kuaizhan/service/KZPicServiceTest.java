package com.kuaizhan.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by zixiong on 2017/4/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class KZPicServiceTest {

    @Resource
    KZPicService kzPicService;

    @Test
    public void download() throws Exception {
    }

    @Test
    public void download1() throws Exception {
    }

    @Test
    public void uploadByPathAndUserId() throws Exception {
    }

    @Test
    public void uploadByUrlAndUserId() throws Exception {
        String url = kzPicService.uploadByUrlAndUserId("http://pic.kuaizhan.com/g1/M00/ED/8A/wKjmqVjnUhuAVcgSAADhAhPTuYs1285317", 123);
        System.out.println("---->" + url);
    }

}