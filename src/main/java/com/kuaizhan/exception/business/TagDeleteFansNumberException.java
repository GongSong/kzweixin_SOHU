package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * Created by liangjiateng on 2017/3/17.
 */
public class TagDeleteFansNumberException extends BaseException{
    public TagDeleteFansNumberException() {
        super(ErrorCodeConfig.TAG_DELETE_FANS_NUMBER_ERROR.getCode(), ErrorCodeConfig.TAG_DELETE_FANS_NUMBER_ERROR.getMsg(), null);
    }
}
