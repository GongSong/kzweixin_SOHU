package com.kuaizhan.exception.deprecated.business;

import com.kuaizhan.config.ErrorCodeConfig;

/**
 * Created by liangjiateng on 2017/3/17.
 */
public class TagDeleteFansNumberException extends BusinessException{
    public TagDeleteFansNumberException() {
        super(ErrorCodeConfig.TAG_DELETE_FANS_NUMBER_ERROR.getCode(), ErrorCodeConfig.TAG_DELETE_FANS_NUMBER_ERROR.getMsg());
    }
}
