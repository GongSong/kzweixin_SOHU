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
        String url = kzPicService.uploadByUrlAndUserId("http://mmbiz.qpic.cn/mmbiz_jpg/eBmuqP7dBC9UaUeLSGJ9sdsDSEGaVQeZpkZmJ485iadUH735UcaYcMKSiaRCJZUxMpiaXicQxdziczPxVzdI8bHN4GQ/640", 123);
        System.out.println("---->" + url);
    }

}