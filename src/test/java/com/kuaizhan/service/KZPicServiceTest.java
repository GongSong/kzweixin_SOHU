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
        String url = kzPicService.uploadByUrlAndUserId("http://mmbiz.qpic.cn/mmbiz/JMbhzHtjFJibsjkeZ7UtIETepDeoKJ94YbjgkHevianqDuMicMiagG3rmKjaRf2Qvv4PbDrbVZovcuoNcIIEoolk7w/640?wx_fmt=jpeg&amp;wxfrom=5&amp;wx_lazy=1", 123);
        System.out.println("---->" + url);
    }

}