package com.kuaizhan.utils;

import com.kuaizhan.manager.KzManager;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zixiong on 2017/4/26.
 */
public class HttpClientUtilTest {
    @Test
    public void get() throws Exception {
        String res = HttpClientUtil.get("http://localhost:8080");
        System.out.println("---->" + res);
    }

    @Test
    public void get1() throws Exception {
    }

    @Test
    public void getFile() throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "pic.t1.com");
        String filepath = HttpClientUtil.getFile("http://10.10.120.180/g1/M00/01/9C/CgoYvFj-rTyAOU-mAAQurpWibVs8508739", headers);
        System.out.println("---->" + filepath);
    }

    @Test
    public void postFile() throws Exception {
        String url = KzManager.uploadPicToKz("http://mmbiz.qpic.cn/mmbiz_gif/FCTGre24cSMiaI01H2IgbkdG0B2FsqQGQ2No2ic19UPpbHTKnGVGgNDoDwbtwu06nxLialjBabNictLuGH05uocbEA/0?wx_fmt=gif");
        System.out.println("---->" + url);
    }
}