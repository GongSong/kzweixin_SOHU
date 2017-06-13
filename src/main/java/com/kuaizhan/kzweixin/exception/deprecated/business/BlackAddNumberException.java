package com.kuaizhan.kzweixin.exception.deprecated.business;

import com.kuaizhan.kzweixin.config.ErrorCodeConfig;

/**
 * Created by liangjiateng on 2017/3/17.
 */
public class BlackAddNumberException extends BusinessException {
    public BlackAddNumberException() {
        super(ErrorCodeConfig.BLACK_ADD_NUMBER_ERROR.getCode(), ErrorCodeConfig.BLACK_ADD_NUMBER_ERROR.getMsg());
    }
}
