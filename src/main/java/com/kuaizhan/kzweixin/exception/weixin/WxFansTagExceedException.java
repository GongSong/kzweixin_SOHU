package com.kuaizhan.kzweixin.exception.weixin;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * Created by fangtianyu on 6/21/17.
 */
public class WxFansTagExceedException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.FANS_TAG_EXCEED;

    public WxFansTagExceedException() {
        super(WxFansTagExceedException.errorCode);
    }

    public WxFansTagExceedException(String message) {
        super(WxFansTagExceedException.errorCode, message);
    }
}