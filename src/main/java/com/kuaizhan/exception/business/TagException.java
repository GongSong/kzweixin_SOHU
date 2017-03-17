package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * 获取标签异常
 * Created by liangjiateng on 2017/3/17.
 */
public class TagException extends BaseException {
    public TagException() {
        super(ErrorCodeConfig.GET_TAG_ERROR.getCode(), ErrorCodeConfig.GET_TAG_ERROR.getMsg(), null);
    }
}
