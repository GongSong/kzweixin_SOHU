package com.kuaizhan.exception.business;

import com.kuaizhan.exception.BaseException;

/**
 * Created by liangjiateng on 2017/3/27.
 */
public class BusinessException extends BaseException {
    public BusinessException(int code, String msg) {
        super(code, msg);
    }
}
