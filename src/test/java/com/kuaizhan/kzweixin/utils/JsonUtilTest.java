package com.kuaizhan.kzweixin.utils;

import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by fty_07 on 8/9/17.
 */
public class JsonUtilTest {
    @Test
    public void string2List() throws Exception {
        String json = "{\"url_list\":[{\"title\":\"hahaha\",\"description\":\"链接测试\",\"picUrl\":\"http://s0.dev.com/res/weixin/images/post-default-cover-900-500.png\",\"url\":\"http://s0.dev.com/res/weixin/images/post-default-cover-900-500.png\",\"linkType\":\"URL\",\"post_title\":\"hahaha\",\"post_description\":\"链接测试\",\"post_pic_url\":\"http://s0.dev.com/res/weixin/images/post-default-cover-900-500.png\",\"post_url\":\"http://s0.dev.com/res/weixin/images/post-default-cover-900-500.png\",\"link_res_type\":1,\"link_res_name\":\"http://s0.dev.com/res/weixin/images/post-default-cover-900-500.png\"}]}";
        Map<String, Object> map = JsonUtil.string2Bean(json, Map.class);
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println(map.get("url_list"));
        ArrayList<Object> arrlist = (ArrayList<Object>)map.get("url_list");
        Map<String, Object> map2 = (Map<String, Object>) arrlist.get(0);
        System.out.println(map2.get("title"));
        System.out.println(map2.get("description"));
        System.out.println(map2.get("link_res_type"));
    }

    @Test
    public void list2Str() throws Exception {
        String json = "{\"ruleName\": \"链接组测试\"}";
        String jsonStr = "{\"ruleName\": \"’‘’‘’''''''''''“”“”“”“”“”“”“\"}";
        JSONObject jsonObject = new JSONObject(jsonStr);
        System.out.println(jsonObject.toString());
    }

    @Test
    public void string2Bean() throws Exception {
    }

    @Test
    public void bean2String() throws Exception {
    }

}