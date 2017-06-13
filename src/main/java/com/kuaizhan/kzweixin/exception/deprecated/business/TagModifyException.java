package com.kuaizhan.kzweixin.exception.deprecated.business;

import com.kuaizhan.kzweixin.config.ErrorCodeConfig;

/**
 * Created by liangjiateng on 2017/3/17.
 */
public class TagModifyException extends BusinessException {
    public TagModifyException() {
        super(ErrorCodeConfig.TAG_MODIFY_ERROR.getCode(), ErrorCodeConfig.TAG_MODIFY_ERROR.getMsg());
    }
}
