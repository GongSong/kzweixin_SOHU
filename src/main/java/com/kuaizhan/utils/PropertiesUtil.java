package com.kuaizhan.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * property资源解析工具类
 * Created by Mr.Jadyn on 2017/2/13.
 */
public final class PropertiesUtil {

    /**
     * 加载属性文件
     *
     * @param fileName
     * @return
     */
    public static Properties loadProps(String fileName) {

        Properties props = null;
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                throw new FileNotFoundException(fileName + " file is not found");
            }
            props = new Properties();
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
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
     * @param props
     * @param key
     * @return
     */
    public static String getString(Properties props, String key) {
        return getString(props, key, "");
    }

    /**
     * 获取字符型属性(可指定默认值)
     * @param props
     * @param key
     * @param defaultVaule
     * @return
     */
    public static String getString(Properties props, String key, String defaultVaule) {
        String value = defaultVaule;
        if (props.containsKey(key)) {
            value = props.getProperty(key);
        }
        return value;
    }

    /**
     * 获取数值型属性(默认值为0)
     * @param props
     * @param key
     * @return
     */
    public static int getInt(Properties props,String key){
        return getInt(props,key,0);

    }

    /**
     * 获取数值型属性(可指定默认值)
     * @param props
     * @param key
     * @param defaultValue
     * @return
     */

    public static int getInt(Properties props, String key, int defaultValue) {
        int value=defaultValue;
        if(props.containsKey(key)){
            value=Integer.parseInt(props.getProperty(key));
        }
        return value;
    }

}
