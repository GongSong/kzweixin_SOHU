package com.kuaizhan.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuaizhan.exception.common.Bean2StringException;
import com.kuaizhan.exception.common.String2BeanException;
import com.kuaizhan.exception.common.String2ListException;


import java.io.IOException;
import java.util.List;

/**
 * json工具
 * Created by czm on 2017/1/18.
 */

public class JsonUtil {

    /**
     * string 转list
     *
     * @param jsonStr
     * @param cls
     * @return
     * @throws IOException
     */
    public static <T> List<T> string2List(String jsonStr, Class<?> cls) throws String2ListException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, cls);
        try {
            return objectMapper.readValue(jsonStr, javaType);
        } catch (IOException e) {
            throw new String2ListException("[String2List] failed, Class: " + cls + " jsonStr" + jsonStr, e);
        }
    }

    public static <T> String list2Str(List<T> list , Class<?> cla) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, cla);
        return objectMapper.writeValueAsString(list);
    }

    /**
     * string 转java bean
     *
     * @param jsonStr
     * @param cls
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T string2Bean(String jsonStr, Class<?> cls) throws String2BeanException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return (T) objectMapper.readValue(jsonStr, cls);
        } catch (IOException e) {
            throw new String2BeanException("[String2Bean] failed, Class:" + cls + " jsonStr" + jsonStr, e);
        }
    }

    /**
     * bean 转java
     *
     * @param bean
     * @param <T>
     * @return
     * @throws JsonProcessingException
     */
    public static <T> String bean2String(T bean) throws Bean2StringException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            throw new Bean2StringException("[bean2String] failed, bean:" + bean, e);
        }
    }

}
