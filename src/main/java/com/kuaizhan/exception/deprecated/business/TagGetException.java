package com.kuaizhan.exception.deprecated.business;

import com.kuaizhan.config.ErrorCodeConfig;

/**
 * 获取标签异常
 * Created by liangjiateng on 2017/3/17.
 */
public class TagGetException extends BusinessException {
    public TagGetException() {
        super(ErrorCodeConfig.GET_TAG_ERROR.getCode(), ErrorCodeConfig.GET_TAG_ERROR.getMsg());
    }
}
