package com.kuaizhan.kzweixin.exception.weixin;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * 错误的App Secret
 * Created by fangtianyu on 6/7/17.
 */
public class WxInvalidAppSecretException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.INVALID_APP_SECRET;

    public WxInvalidAppSecretException() {
        super(WxInvalidAppSecretException.errorCode);
    }

    public WxInvalidAppSecretException(String message) {
        super(WxInvalidAppSecretException.errorCode, message);
    }
}
