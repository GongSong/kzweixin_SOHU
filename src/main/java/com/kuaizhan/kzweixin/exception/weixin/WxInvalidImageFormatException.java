package com.kuaizhan.kzweixin.exception.weixin;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * Created by zixiong on 2017/5/31.
 */
public class WxInvalidImageFormatException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.INVALID_IMAGE_FORMAT;

    public WxInvalidImageFormatException() {
        super(WxInvalidImageFormatException.errorCode);
    }
}
