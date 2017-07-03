package com.kuaizhan.kzweixin.exception.weixin;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * Created by fangtianyu on 6/16/17.
 */
public class WxTagReservedModifiedException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.INVALID_TAG_MODIFIED;

    public WxTagReservedModifiedException() {
        super(WxTagReservedModifiedException.errorCode);
    }

    public WxTagReservedModifiedException(String message) {
        super(WxTagReservedModifiedException.errorCode, message);
    }
}
