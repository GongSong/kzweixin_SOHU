package com.kuaizhan.kzweixin.manager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by zixiong on 2017/5/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class KzManagerTest {

    @Resource
    KzManager kzManager;

    @Test
    public void getKzArticle() throws Exception {
        System.out.println("---->" + kzManager.uploadPicToKz("http://pic.kuaizhan.com/g1/M00/51/D6/CgpQU1kSz5iAawy1AAAY8uB6Rgs3135599/imageView/v1/thumbnail/200x200", 1));
    }

    @Test
    public void uploadPicToKz() throws Exception {

    }

}