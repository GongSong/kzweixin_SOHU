package com.kuaizhan.kzweixin.exception.deprecated.business;

import com.kuaizhan.kzweixin.exception.BaseException;

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
