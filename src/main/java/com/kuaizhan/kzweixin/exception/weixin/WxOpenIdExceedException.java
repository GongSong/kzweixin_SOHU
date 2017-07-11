package com.kuaizhan.kzweixin.exception.weixin;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * Created by fangtianyu on 6/21/17.
 */
public class WxOpenIdExceedException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.OPEN_ID_EXCEED;

    public WxOpenIdExceedException() {
        super(WxOpenIdExceedException.errorCode);
    }

    public WxOpenIdExceedException(String message) {
        super(WxOpenIdExceedException.errorCode, message);
    }
}