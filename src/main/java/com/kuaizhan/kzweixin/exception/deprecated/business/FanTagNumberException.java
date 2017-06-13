package com.kuaizhan.kzweixin.exception.deprecated.business;

import com.kuaizhan.kzweixin.config.ErrorCodeConfig;

/**
 * Created by liangjiateng on 2017/3/17.
 */
public class FanTagNumberException extends BusinessException {
    public FanTagNumberException() {
        super(ErrorCodeConfig.FAN_TAG_NUMBER_ERROR.getCode(), ErrorCodeConfig.FAN_TAG_NUMBER_ERROR.getMsg());
    }
}
