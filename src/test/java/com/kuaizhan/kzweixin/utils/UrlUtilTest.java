package com.kuaizhan.kzweixin.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by zixiong on 2017/4/18.
 */
public class UrlUtilTest {
    @Test
    public void fixProtocol() throws Exception {
    }

    @Test
    public void getPicIntranetAddress() throws Exception {
        System.out.println("---->" + UrlUtil.getPicIntranetAddress("pic.t1.com/aaa/bbb"));
    }

    @Test
    public void removeUrlPrefixIfExists() throws Exception {
        String prefix = "prefix";
        String content = "content" + prefix;
        System.out.println("---->" + UrlUtil.removePrefixIfExists(prefix + content, prefix));
        assertEquals(UrlUtil.removePrefixIfExists(prefix + content, prefix), content);
    }

    @Test
    public void removeUrlSuffixIfExists() throws Exception {
        String suffix = "suffix";
        String content = suffix +"fda3vcdfasefel" + suffix;
        assertEquals(content, UrlUtil.removeSuffixIfExists(content + suffix, suffix));
    }

    @Test
    public void fixQuote() throws Exception{
        String tmp = "http:haha.com";
        assertEquals(tmp, UrlUtil.fixQuote("\"" + tmp + "\""));
    }

}