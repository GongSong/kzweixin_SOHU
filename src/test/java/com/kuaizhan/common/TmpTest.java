package com.kuaizhan.common;

import com.kuaizhan.utils.JsonUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.*;


/**
 * 临时测试入口, 不用加载spring的测试
 * Created by zixiong on 2017/4/12.
 */
public class TmpTest {

    @Test
    public void test() throws Exception {
        String title = "我带了双引号”“";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("title", "我带了双引号\\u201d\\u201c");

        System.out.println("---->" + jsonObject.toString());
        System.out.println("---->" + jsonObject1.toString());
        System.out.println(Objects.equals("---->" + jsonObject.toString(), jsonObject1.toString()));

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);

        JSONObject out = new JSONObject();
        out.put("articles", jsonArray);

        System.out.println("---->" + JsonUtil.bean2String(out.toMap()));
    }
}
