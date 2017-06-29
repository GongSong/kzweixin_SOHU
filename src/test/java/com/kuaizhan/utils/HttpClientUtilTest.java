package com.kuaizhan.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.junit.Test;

import java.io.File;
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
        HttpResponse<JsonNode> jsonResponse = Unirest.post("http://cos.kuaizhan.sohuno.com/api/v2/upload")
                .field("file", new File("/Users/zixiong/Desktop/0.jpeg"))
                .asJson();
        System.out.println("---->" + jsonResponse);
    }
}