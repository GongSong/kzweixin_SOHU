package com.kuaizhan.exception.common;

/**
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
