package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * Created by liangjiateng on 2017/3/17.
 */
public class OpenIdException extends BusinessException {
    public OpenIdException() {
        super(ErrorCodeConfig.OPEN_ID_ERROR.getCode(), ErrorCodeConfig.OPEN_ID_ERROR.getMsg());
    }
}
