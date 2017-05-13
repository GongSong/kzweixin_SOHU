package com.kuaizhan.exception.weixin;

/**
 * 微信获取图文列表失败
 * Created by lorin on 17-3-29.
 */
public class WxPostListGetException extends RuntimeException {
    public WxPostListGetException(String msg) {
        super(msg);
    }
    public WxPostListGetException(String msg, Exception cause) {
        super(msg, cause);
    }
}
