package com.kuaizhan.exception.common;

/**
 * Created by zixiong on 2017/5/9.
 */
public class String2BeanFailedException extends RuntimeException {
    public String2BeanFailedException(String msg, Exception cause) {
        super(msg, cause);
    }
}
