package com.kuaizhan.exception.system;

import com.kuaizhan.config.ErrorCodeConfig;

/**
 * redis缓存异常
 * Created by liangjiateng on 2017/3/15.
 */
public class RedisException extends SystemException {
    public RedisException(Exception e) {
        super(ErrorCodeConfig.REDIS_ERROR.getCode(), ErrorCodeConfig.REDIS_ERROR.getMsg(), e);
    }
}
