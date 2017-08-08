package com.kuaizhan.kzweixin.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuaizhan.kzweixin.exception.common.JsonConvertException;
import com.fasterxml.jackson.core.JsonParser.Feature;

import java.io.IOException;
import java.util.List;

/**
 * json工具
 * Created by czm on 2017/1/18.
 */

public class JsonUtil {

    /**
     * json string 转list
     * @throws JsonConvertException string格式不对
     */
    public static <T> List<T> string2List(String jsonStr, Class<?> cls) throws JsonConvertException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, cls);
        try {
            return objectMapper.readValue(jsonStr, javaType);
        } catch (IOException e) {
            throw new JsonConvertException("[String2List] failed, Class: " + cls + " jsonStr" + jsonStr, e);
        }
    }

    /**
     * list 转json string
     * @throws JsonConvertException
     */
    public static <T> String list2Str(List<T> list) throws JsonConvertException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new JsonConvertException("[List2String] failed, list:" + list, e);
        }
    }

    /**
     * string 转java bean
     * @throws JsonConvertException
     */
    // TODO: 研究下，string2Bean并不强制返回class的类型
    public static <T> T string2Bean(String jsonStr, Class<?> cls) throws JsonConvertException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        try {
            return (T) objectMapper.readValue(jsonStr, cls);
        } catch (IOException e) {
            throw new JsonConvertException("[String2Bean] failed, Class:" + cls + " jsonStr" + jsonStr, e);
        }
    }

    /**
     * bean 转java
     * @throws JsonConvertException
     */
    public static <T> String bean2String(T bean) throws JsonConvertException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            throw new JsonConvertException("[bean2String] failed, bean:" + bean, e);
        }
    }

}
