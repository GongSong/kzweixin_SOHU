package com.kuaizhan.utils;

import com.kuaizhan.config.ApplicationConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lorin on 17-3-31.
 */
public class UrlUtil {

    /**
     * 补全网址协议
     *
     * @param url
     */
    public static String fixProtocol(String url) {
        return url.startsWith("//") ? "http:" + url : url;
    }


    /**
     * 把图片外网域名转换为内网, 不能转换则原封返回
     * 只转换线上环境的
     */
    public static Map<String ,String> getPicIntranetAddress(String url){
        Map<String, String> address = new HashMap<>();

        String picHost = ApplicationConfig.getPicHost();
        String replaceHost = ApplicationConfig.getPicReplaceHost();
        String picIp = ApplicationConfig.getPicIp();

        if (url.contains(picHost)){
            address.put("url", url.replaceAll(picHost, picIp));
            address.put("host", replaceHost);
        } else {
            address.put("url", url);
            address.put("host", null);
        }
        return address;
    }
}