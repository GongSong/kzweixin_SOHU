package com.kuaizhan.exception.common;


/**
 * redis缓存异常
 * Created by liangjiateng on 2017/3/15.
 */
public class RedisException extends RuntimeException {
    public RedisException(Throwable cause) {
        super(cause);
    }
}
