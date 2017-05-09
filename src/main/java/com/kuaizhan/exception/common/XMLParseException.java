package com.kuaizhan.exception.common;

/**
 * xml解析异常
 * Created by liangjiateng on 2017/3/15.
 */
public class XMLParseException extends RuntimeException {
    public XMLParseException(Throwable cause) {
        super(cause);
    }
    public XMLParseException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
