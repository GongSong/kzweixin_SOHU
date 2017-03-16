package com.kuaizhan.exception.system;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * 加密异常
 * Created by liangjiateng on 2017/3/15.
 */
public class EncryptException extends BaseException {
    public EncryptException(String errorStack) {
        super(ErrorCodeConfig.ENCRYPT_ERROR.getCode(), ErrorCodeConfig.ENCRYPT_ERROR.getMsg(), errorStack);
    }
}
