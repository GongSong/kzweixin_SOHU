package com.kuaizhan.utils;

/**
 * Created by lorin on 17-3-31.
 */
public class UrlUtil {

    /**
     * 补全网址协议
     *
     * @param url
     * @param protocol
     * @return
     */
    public static String fixProtocol(String url, String protocol) {
        return url.startsWith("//") ? protocol + url : url;
    }

    /**
     * 默认补全http
     *
     * @param url
     * @return
     */
    public static String fixProtocol(String url) {
        return fixProtocol(url, "http");
    }

}
