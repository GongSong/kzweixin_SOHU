package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * 重复标签异常
 * Created by liangjiateng on 2017/3/17.
 */
public class TagDuplicateNameException extends BaseException {
    public TagDuplicateNameException() {
        super(ErrorCodeConfig.TAG_DUPLICATE_NAME_ERROR.getCode(), ErrorCodeConfig.TAG_DUPLICATE_NAME_ERROR.getMsg(), null);
    }
}
