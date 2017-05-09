package com.kuaizhan.exception.deprecated.system;

import com.kuaizhan.config.ErrorCodeConfig;

/**
 * 加密异常
 * Created by liangjiateng on 2017/3/15.
 */
public class EncryptException extends SystemException {
    public EncryptException(Exception e) {
        super(ErrorCodeConfig.ENCRYPT_ERROR.getCode(), ErrorCodeConfig.ENCRYPT_ERROR.getMsg(), e);
    }
}
