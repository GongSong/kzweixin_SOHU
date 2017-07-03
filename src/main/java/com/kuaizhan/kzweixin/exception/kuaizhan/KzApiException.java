package com.kuaizhan.kzweixin.exception.kuaizhan;

/**
 * Created by zixiong on 2017/5/31.
 */
public class KzApiException extends RuntimeException {
    public KzApiException(String msg) {
        super(msg);
    }
    public KzApiException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
