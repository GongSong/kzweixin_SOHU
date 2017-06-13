package com.kuaizhan.kzweixin.exception.deprecated.business;

import com.kuaizhan.kzweixin.config.ErrorCodeConfig;

/**
 * 参数异常
 * Created by liangjiateng on 2017/3/15.
 */
public class ParamException extends BusinessException {

    public ParamException() {
        super(ErrorCodeConfig.PARAM_ERROR.getCode(), ErrorCodeConfig.PARAM_ERROR.getMsg());
    }

    public ParamException (String msg) {
        super(ErrorCodeConfig.PARAM_ERROR.getCode(), msg);
    }

}
