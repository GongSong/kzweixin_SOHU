package com.kuaizhan.kzweixin.utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zixiong on 2017/08/01.
 */
public class ParamUtil {

    /**
     * 从json字符串获取list，或者以','号为分隔符的字符串
     */
    public static List<Integer> getIntList(String listStr) {
        List<Integer> result = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(listStr);
            for (int i = 0; i < jsonArray.length(); i ++) {
                result.add(jsonArray.getInt(i));
            }
        // 抛异常说明不是json list
        } catch (JSONException e) {
            String[] intStrList = listStr.split(",");
            for (String str: intStrList) {
                result.add(Integer.parseInt(str));
            }
        }
        return result;
    }
}
