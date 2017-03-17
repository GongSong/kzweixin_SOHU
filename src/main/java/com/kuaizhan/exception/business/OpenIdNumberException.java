package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * Created by liangjiateng on 2017/3/17.
 */
public class OpenIdNumberException extends BaseException {
    public OpenIdNumberException() {
        super(ErrorCodeConfig.OPEN_ID_NUMBER_ERROR.getCode(), ErrorCodeConfig.OPEN_ID_NUMBER_ERROR.getMsg(),null);
    }
}
