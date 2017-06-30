package com.kuaizhan.kzweixin.exception.weixin;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * Created by fangtianyu on 6/21/17.
 */
public class WxOpenIdMismatchException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.OPEN_ID_MISMATCH_ERROR;

    public WxOpenIdMismatchException() {
        super(WxOpenIdMismatchException.errorCode);
    }

    public WxOpenIdMismatchException(String message) {
        super(WxOpenIdMismatchException.errorCode, message);
    }
}