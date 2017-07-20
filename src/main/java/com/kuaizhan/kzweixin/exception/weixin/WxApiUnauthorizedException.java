package com.kuaizhan.kzweixin.exception.weixin;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * Created by zixiong on 2017/6/29.
 */
public class WxApiUnauthorizedException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.API_UNAUTHORIZED;

    public WxApiUnauthorizedException() {
        super(WxApiUnauthorizedException.errorCode);
    }

    public WxApiUnauthorizedException(String msg) {
        super(WxApiUnauthorizedException.errorCode, msg);
    }
}
