package com.kuaizhan.kzweixin.exception.deprecated.business;

import com.kuaizhan.kzweixin.config.ErrorCodeConfig;

/**
 * 重复标签异常
 * Created by liangjiateng on 2017/3/17.
 */
public class TagDuplicateNameException extends BusinessException {
    public TagDuplicateNameException() {
        super(ErrorCodeConfig.TAG_DUPLICATE_NAME_ERROR.getCode(), ErrorCodeConfig.TAG_DUPLICATE_NAME_ERROR.getMsg());
    }
}
