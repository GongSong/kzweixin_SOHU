package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * Created by liangjiateng on 2017/3/17.
 */
public class TagException extends BusinessException{
    public TagException() {
        super(ErrorCodeConfig.TAG_ERROR.getCode(), ErrorCodeConfig.TAG_ERROR.getMsg());
    }
}
