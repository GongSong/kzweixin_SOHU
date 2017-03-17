package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * 标签名长度异常
 * Created by liangjiateng on 2017/3/17.
 */
public class TagNameLengthException extends BaseException {
    public TagNameLengthException() {
        super(ErrorCodeConfig.TAG_NAME_LENGTH_ERROR.getCode(), ErrorCodeConfig.TAG_NAME_LENGTH_ERROR.getMsg(), null);
    }
}
