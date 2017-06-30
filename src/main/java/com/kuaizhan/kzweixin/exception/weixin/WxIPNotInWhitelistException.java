package com.kuaizhan.kzweixin.exception.weixin;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * IP未被设置为白名单
 * Created by fangtianyu on 6/7/17.
 */
public class WxIPNotInWhitelistException extends BusinessException{
    private static final ErrorCode errorCode = ErrorCode.IP_NOT_IN_WHITELIST;

    public WxIPNotInWhitelistException() {
        super(WxIPNotInWhitelistException.errorCode);
    }

    public WxIPNotInWhitelistException(String message) {
        super(WxIPNotInWhitelistException.errorCode, message);
    }
}
