package com.kuaizhan.utils;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * property资源解析工具类
 * Created by Mr.Jadyn on 2017/2/13.
 */
public final class PropertiesUtil {

    private static final Logger logger = Logger.getLogger(PropertiesUtil.class);

    /**
     * 加载属性文件
     */
    public static Properties loadProps(String fileName) {

        Properties props = null;
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                throw new FileNotFoundException("[PropertiesUtil] file is not found, fileName:" + fileName);
            }
            props = new Properties();
            props.load(is);
        } catch (IOException e) {
            logger.error("[PropertiesUtil] loadProps failed, fileName" + fileName, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                   e.printStackTrace();
                }
            }
        }
        return props;
    }

    /**
     * 获取字符型属性(默认值为空)
     */
    public static String getString(Properties props, String key) {
        return getString(props, key, "");
    }

    /**
     * 获取字符型属性(可指定默认值)
     */
    public static String getString(Properties props, String key, String defaultVaule) {
        if (props.containsKey(key)) {
            return props.getProperty(key);
        } else {
            return defaultVaule;
        }
    }

    /**
     * 获取int型属性(默认值为0)
     */
    public static int getInt(Properties props, String key){
        return getInt(props, key,0);

    }

    /**
     * 获取int型属性(可指定默认值)
     */

    public static int getInt(Properties props, String key, int defaultValue) {
        if (props.containsKey(key)) {
            return Integer.parseInt(props.getProperty(key));
        } else {
            return defaultValue;
        }
    }

    /**
     * 获取long型属性(默认值0)
     */
    public static long getLong(Properties props, String key) {
        return getLong(props, key, 0);
    }

    /**
     * 获取long型属性（可指定默认值）
     */
    public static long getLong(Properties props, String key, long defaultValue) {
        if (props.containsKey(key)) {
            return Long.parseLong(props.getProperty(key));
        } else {
            return defaultValue;
        }
    }

}
