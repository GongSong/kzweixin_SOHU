package com.kuaizhan.exception.deprecated.business;

import com.kuaizhan.exception.BaseException;

/**
 * Created by liangjiateng on 2017/3/27.
 */
public class BusinessException extends BaseException {
    public BusinessException(int code, String msg) {
        super(code, msg);
    }
    public BusinessException(int code, String msg, Exception e) {
        super(code, msg, e);
    }
}
