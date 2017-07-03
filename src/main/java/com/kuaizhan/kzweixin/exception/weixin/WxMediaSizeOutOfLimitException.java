package com.kuaizhan.kzweixin.exception.weixin;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * Created by zixiong on 2017/5/31.
 */
public class WxMediaSizeOutOfLimitException extends BusinessException {

    private static final ErrorCode errorCode = ErrorCode.MEDIA_SIZE_OUT_OF_LIMIT;

    public WxMediaSizeOutOfLimitException() {
        super(WxMediaSizeOutOfLimitException.errorCode);
    }
}
