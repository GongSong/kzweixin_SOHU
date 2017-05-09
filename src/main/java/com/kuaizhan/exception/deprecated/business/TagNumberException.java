package com.kuaizhan.exception.deprecated.business;

import com.kuaizhan.config.ErrorCodeConfig;

/**
 * Created by liangjiateng on 2017/3/17.
 */
public class TagNumberException extends BusinessException {
    public TagNumberException() {
        super(ErrorCodeConfig.TAG_NUMBER_ERROR.getCode(), ErrorCodeConfig.TAG_NUMBER_ERROR.getMsg());
    }
}
