package com.kuaizhan.kzweixin.exception.deprecated.business;

import com.kuaizhan.kzweixin.config.ErrorCodeConfig;

/**
 * Created by liangjiateng on 2017/3/17.
 */
public class OpenIdNumberException extends BusinessException {
    public OpenIdNumberException() {
        super(ErrorCodeConfig.OPEN_ID_NUMBER_ERROR.getCode(), ErrorCodeConfig.OPEN_ID_NUMBER_ERROR.getMsg());
    }
}
