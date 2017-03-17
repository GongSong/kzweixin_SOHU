package com.kuaizhan.exception.system;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * Created by liangjiateng on 2017/3/17.
 */
public class ServerException extends BaseException {
    public ServerException(String errorStack) {
        super(ErrorCodeConfig.SERVER_ERROR.getCode(), ErrorCodeConfig.SERVER_ERROR.getMsg(), errorStack);
    }
}
