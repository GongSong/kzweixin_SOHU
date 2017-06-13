package com.kuaizhan.kzweixin.exception.deprecated.business;

import com.kuaizhan.kzweixin.config.ErrorCodeConfig;

/**
 * Created by liangjiateng on 2017/3/17.
 */
public class OpenIdException extends BusinessException {
    public OpenIdException() {
        super(ErrorCodeConfig.OPEN_ID_ERROR.getCode(), ErrorCodeConfig.OPEN_ID_ERROR.getMsg());
    }
}
