package com.kuaizhan.exception.common;


/**
 * 解密异常
 * Created by liangjiateng on 2017/3/15.
 */
public class DecryptException extends RuntimeException {
    public DecryptException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
