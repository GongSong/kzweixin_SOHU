package com.kuaizhan.utils;

import com.kuaizhan.config.ApplicationConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lorin on 17-3-31.
 */
public class UrlUtil {

    /**
     * 补全网址协议
     */
    public static String fixProtocol(String url) {
        return url.startsWith("//") ? "http:" + url : url;
    }

    /**
     * 删除url前后的单双引号
     *
     * @return
     */
    public static String fixQuote(String url) {
        url = removePrefixIfExists(url,"&quot;");
        url = removeSuffixIfExists(url, "&quot;");
        return url;
    }

    /**
     * 去除某字符串前缀
     *
     * @param str
     * @param prefix
     * @return
     */
    public static String removePrefixIfExists(String str, String prefix) {
        if (str == null || prefix == null) return str;

        if (str.startsWith(prefix)) {
            str = str.substring(prefix.length());
        }

        return str;
    }

    /**
     * 去除某字符串后缀
     *
     * @param str
     * @param suffix
     * @return
     */
    public static String removeSuffixIfExists(String str, String suffix) {
        if (str == null || suffix == null) return str;

        if (str.endsWith(suffix)) {
            str = str.substring(0, str.lastIndexOf(suffix));
        }

        return str;
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