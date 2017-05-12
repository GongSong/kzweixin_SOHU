package com.kuaizhan.exception.common;

/**
 * Created by zixiong on 2017/5/9.
 */
public class String2BeanException extends RuntimeException {
    public String2BeanException(String msg, Exception cause) {
        super(msg, cause);
    }
}
