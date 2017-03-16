package com.kuaizhan.exception.system;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * redis缓存异常
 * Created by liangjiateng on 2017/3/15.
 */
public class RedisException extends BaseException {
    public RedisException(String errorStack) {
        super(ErrorCodeConfig.REDIS_ERROR.getCode(), ErrorCodeConfig.REDIS_ERROR.getMsg(), errorStack);
    }
}
