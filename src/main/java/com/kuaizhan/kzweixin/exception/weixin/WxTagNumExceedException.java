package com.kuaizhan.kzweixin.exception.weixin;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * 创建的新标签数量超过限制
 * Created by fangtianyu on 6/15/17.
 */
public class WxTagNumExceedException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.INVALID_TAG_NUM;

    public WxTagNumExceedException() {
        super(WxTagNumExceedException.errorCode);
    }

    public WxTagNumExceedException(String message) {
        super(WxTagNumExceedException.errorCode, message);
    }
}
