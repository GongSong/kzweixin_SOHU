package com.kuaizhan.kzweixin.exception.common;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * 参数错误
 * Created by zixiong on 2017/07/25.
 */
public class ParamException extends BusinessException {
    private static final ErrorCode errorcode = ErrorCode.PARAM_ERROR;

    public ParamException() {
        super(ParamException.errorcode);
    }

    public ParamException(String msg) {
        super(ParamException.errorcode, msg);
    }
}
