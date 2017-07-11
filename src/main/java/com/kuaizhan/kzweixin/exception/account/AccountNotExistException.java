package com.kuaizhan.kzweixin.exception.account;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * 公众号不存在
 * Created by zixiong on 2017/7/7.
 */
public class AccountNotExistException extends BusinessException {
    public static final ErrorCode errorCode = ErrorCode.ACCOUNT_NOT_EXIST;

    public AccountNotExistException() {
        super(AccountNotExistException.errorCode);
    }
}
