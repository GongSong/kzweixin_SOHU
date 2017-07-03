package com.kuaizhan.kzweixin.exception.weixin;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * 创建的新标签和原有标签重名
 * Created by fangtianyu on 6/15/17.
 */
public class WxDuplicateTagException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.DUPLICATED_TAG;

    public WxDuplicateTagException() {
        super(WxDuplicateTagException.errorCode);
    }

    public WxDuplicateTagException(String message) {
        super(WxDuplicateTagException.errorCode, message);
    }
}
