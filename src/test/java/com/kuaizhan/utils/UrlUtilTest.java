package com.kuaizhan.utils;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

/**
 * Created by zixiong on 2017/4/18.
 */
public class UrlUtilTest {
    @Test
    public void fixProtocol() throws Exception {
        System.out.println("---->" + UrlUtil.fixProtocol("//www.baidu.com//fdafefdqa"));
    }

    @Test
    public void getPicIntranetAddress() throws Exception {
        System.out.println("---->" + UrlUtil.getPicIntranetAddress("pic.t1.com/aaa/bbb"));
    }
}