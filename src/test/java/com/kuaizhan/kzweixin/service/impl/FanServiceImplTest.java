package com.kuaizhan.kzweixin.service.impl;

import org.junit.Test;

import javax.annotation.Resource;
import com.kuaizhan.kzweixin.service.FanService;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by fangtianyu on 6/29/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class FanServiceImplTest {
    @Resource
    private FanService fanService;

    @Test
    public void addFanOpenId() throws Exception {
        String appId = "wx118d7fb9ab2c9f8a";
        String openId = "";
        fanService.addFanOpenId(appId, openId);
    }

    @Test
    public void delFanOpenId() throws Exception {
        String appId = "wx118d7fb9ab2c9f8a";
        String openId = "oBGGJt0uw6Eznx6VKK7Qvftngt9g";
        fanService.delFanOpenId(appId, openId);
    }

    @Test
    public void addFan() throws Exception {
        String appId = "wx1a4ff9ec0e369bd1";
        String openId = "oBGGJt-_qLdxlawjs3CjGcb6xrlQ";
        fanService.addFan(appId, openId);
    }

}