package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * Created by liangjiateng on 2017/3/17.
 */
public class TagModifyException extends BaseException {
    public TagModifyException() {
        super(ErrorCodeConfig.TAG_MODIFY_ERROR.getCode(), ErrorCodeConfig.TAG_MODIFY_ERROR.getMsg());
    }
}
