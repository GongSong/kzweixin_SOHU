package com.kuaizhan.exception.system;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * 解密异常
 * Created by liangjiateng on 2017/3/15.
 */
public class DecryptException extends BaseException {
    public DecryptException(String errorStack) {
        super(ErrorCodeConfig.DECRYPT_ERROR.getCode(), ErrorCodeConfig.ENCRYPT_ERROR.getMsg(), errorStack);
    }
}
