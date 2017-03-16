package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * 参数异常
 * Created by liangjiateng on 2017/3/15.
 */
public class ParamException extends BaseException {

    public ParamException() {
        super(ErrorCodeConfig.PARAM_ERROR.getCode(), ErrorCodeConfig.PARAM_ERROR.getMsg(),null);
    }

    public ParamException(String errorStack) {
        super(ErrorCodeConfig.PARAM_ERROR.getCode(), ErrorCodeConfig.PARAM_ERROR.getMsg(), errorStack);
    }
}
