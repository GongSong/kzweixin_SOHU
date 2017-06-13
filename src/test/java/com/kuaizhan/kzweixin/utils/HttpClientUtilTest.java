package com.kuaizhan.kzweixin.utils;

import com.kuaizhan.kzweixin.config.WxApiConfig;
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
        String fileUrl = "http://pic.kuaizhan.com/g2/M00/38/10/CgpQVFYawpuAYVC6AALqWSZDYzw3840099";
        String fileHost = "pic.kuaizhan.com";
        String token = "rSTqmdGkavwEabhhY4WIgtMA5hBE62yTsmtt8dAQq69DjZdIk331i-d454KSsXiM8laxd4InKHPLTjEWFzfWy_rRJsTWjktxKgsSLupdLDhKYu2YaV4TEVpsGYORF8GUPBCbAKDVHI";
        String url = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + token;
        String res = HttpClientUtil.postFile(WxApiConfig.addMaterialUrl(token, "image"), fileUrl, fileHost);
        System.out.println("---->" + res);
    }

}