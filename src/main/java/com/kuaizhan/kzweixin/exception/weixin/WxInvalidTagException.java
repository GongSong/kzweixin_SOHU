package com.kuaizhan.kzweixin.exception.weixin;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * Created by fangtianyu on 6/21/17.
 */
public class WxInvalidTagException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.INVALID_TAG_ERROR;

    public WxInvalidTagException() {
        super(WxInvalidTagException.errorCode);
    }

    public WxInvalidTagException(String message) {
        super(WxInvalidTagException.errorCode, message);
    }

}
