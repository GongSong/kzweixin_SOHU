package com.kuaizhan.kzweixin.exception.weixin;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * Created by zixiong on 2017/5/18.
 */
public class WxInvalidOpenIdException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.INVALID_OPEN_ID_ERROR;

    public WxInvalidOpenIdException() {
        super(WxInvalidOpenIdException.errorCode);
    }

    public WxInvalidOpenIdException(String message) {
        super(WxInvalidOpenIdException.errorCode, message);
    }
}
