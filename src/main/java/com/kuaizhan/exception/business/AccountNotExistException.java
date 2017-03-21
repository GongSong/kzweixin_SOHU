package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * Created by liangjiateng on 2017/3/16.
 */
public class AccountNotExistException extends BaseException {
    public AccountNotExistException() {
        super(ErrorCodeConfig.ACCOUNT_NULL_ERROR.getCode(), ErrorCodeConfig.ACCOUNT_NULL_ERROR.getMsg());
    }
}
