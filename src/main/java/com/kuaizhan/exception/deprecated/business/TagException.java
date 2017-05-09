package com.kuaizhan.exception.deprecated.business;

import com.kuaizhan.config.ErrorCodeConfig;

/**
 * Created by liangjiateng on 2017/3/17.
 */
public class TagException extends BusinessException{
    public TagException() {
        super(ErrorCodeConfig.TAG_ERROR.getCode(), ErrorCodeConfig.TAG_ERROR.getMsg());
    }
}
