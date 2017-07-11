package com.kuaizhan.kzweixin.exception.weixin;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * Created by fangtianyu on 6/16/17.
 */
public class WxFansNumExceedException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.DELETE_TAG_FANS_EXCEED_10W;

    public WxFansNumExceedException() {
        super(WxFansNumExceedException.errorCode);
    }

    public WxFansNumExceedException(String message) {
        super(WxFansNumExceedException.errorCode, message);
    }
}
