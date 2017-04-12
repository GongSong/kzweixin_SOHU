package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * Created by liangjiateng on 2017/3/17.
 */
public class FanTagNumberException extends BusinessException {
    public FanTagNumberException() {
        super(ErrorCodeConfig.FAN_TAG_NUMBER_ERROR.getCode(), ErrorCodeConfig.FAN_TAG_NUMBER_ERROR.getMsg());
    }
}
