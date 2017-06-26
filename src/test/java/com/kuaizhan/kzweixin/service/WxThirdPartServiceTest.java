package com.kuaizhan.kzweixin.service;

import com.kuaizhan.kzweixin.manager.WxThirdPartManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by zixiong on 2017/6/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class WxThirdPartServiceTest {
    @Resource
    private WxThirdPartService wxThirdPartService;

    @Test
    public void decryptMsg() throws Exception {
        String signature = "176ca0f8c3ec434a9eb6a7acfe93b3643aebfdc5";
        String timestamp = "1498221443";
        String nonce = "1609681122";
        String str = "<xml>\n" +
                "    <ToUserName><![CDATA[gh_c480b0a49842]]></ToUserName>\n" +
                "    <Encrypt><![CDATA[0ApIJHv8hXMbH17e9wd8bkvesM1xNmbRtKaUQwQoX+IETw/YfbcKsPWDSn32r/hk1/W0v0vIKVMSPva74iN1Ptt/AskOzKx1LqQkhN6gaWRGk+1RxOnsFv33bcoWEGtJHP/5Ix8P0c60Z5GZZkLNyX4FsxZumPHDelLn0yZauiWTSTunMwid9aUrwldg2gacCOktmtrpejFkbgz5W7JPVrMjeB+OQHSkxERq0L/S+UQoFyLmmOyEdU6TQtEgQjPqdwcjrxvRoUYfkg9rj3gJWP+WKhfuVLKQxt7l8B4nCsYhRrxjo9lPM12vET8zns6Uomq+qv2YuwV1nVUDb3Jg5apQDU8ZYVb0gmsDnVwNvqwcX9p4L4mM5JSOCsccST/fwP4K5iyHXr3IT0wXh45rShdbbwzGfG6b62uuuMgXOnA=]]></Encrypt>\n" +
                "</xml>";
        String msg = wxThirdPartService.decryptMsg(signature, timestamp, nonce, str);
        System.out.println("---->" + msg);
    }

}