package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * Created by liangjiateng on 2017/3/17.
 */
public class TagNumberException extends BusinessException {
    public TagNumberException() {
        super(ErrorCodeConfig.TAG_NUMBER_ERROR.getCode(), ErrorCodeConfig.TAG_NUMBER_ERROR.getMsg());
    }
}
