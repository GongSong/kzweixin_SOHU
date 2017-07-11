package com.kuaizhan.kzweixin.exception.weixin;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * Created by fangtianyu on 6/23/17.
 */
public class WxBlacklistExceedException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.ADD_BLACKLIST_EXCEED_LIMIT;

    public WxBlacklistExceedException() {
        super(WxBlacklistExceedException.errorCode);
    }

    public WxBlacklistExceedException(String message) {
        super(WxBlacklistExceedException.errorCode, message);
    }
}