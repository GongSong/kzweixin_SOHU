package com.kuaizhan.utils;

import com.kuaizhan.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lorin on 17-3-31.
 */
public class UrlUtil {

    private static final Logger logger = LoggerFactory.getLogger(UrlUtil.class);

    /**
     * 补全网址协议
     */
    public static String fixProtocol(String url) {
        if (url == null){
            return null;
        }
        return url.startsWith("//") ? "http:" + url : url;
    }

    /**
     * 删除url前后的单双引号
     *
     * @return
     */
    public static String fixQuote(String url) {
        url = removePrefixIfExists(url,"&quot;");
        url = removePrefixIfExists(url,"\"");
        url = removeSuffixIfExists(url, "&quot;");
        url = removeSuffixIfExists(url, "\"");
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

        String picHost = ApplicationConfig.KZ_PIC_HOST;
        String replaceHost = ApplicationConfig.KZ_PIC_REPLACE_HOST;
        String picIp = ApplicationConfig.KZ_PIC_IP;

        if (url.contains(picHost)){
            url = url.replaceAll(picHost, picIp);
            // 把内网地址的https换成http
            url = url.replaceAll("https", "http");

            address.put("url", url);
            address.put("host", replaceHost);
        } else {
            address.put("url", url);
            address.put("host", null);
        }
        return address;
    }

    public static String encode(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // never happen !
            logger.error("[UrlUtil: encode] unsupport encode utf-8");
            throw new RuntimeException("[UrlUtil: encode] unsupport encode utf-8");
        }
    }
}