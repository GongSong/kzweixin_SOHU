package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * Created by liangjiateng on 2017/3/17.
 */
public class BlackAddNumberException extends BusinessException {
    public BlackAddNumberException() {
        super(ErrorCodeConfig.BLACK_ADD_NUMBER_ERROR.getCode(), ErrorCodeConfig.BLACK_ADD_NUMBER_ERROR.getMsg());
    }
}
