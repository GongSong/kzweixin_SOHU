package com.kuaizhan.kzweixin.exception.common;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * Created by fangtianyu on 7/3/17.
 */
public class InvalidBase64PicException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.INVALID_BASE64_PIC;

    public InvalidBase64PicException(String msg) {
        super(InvalidBase64PicException.errorCode,  msg);
    }
}
