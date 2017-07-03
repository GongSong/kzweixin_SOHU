package com.kuaizhan.kzweixin.exception.weixin;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * Created by zixiong on 2017/6/28.
 */
public class WxPostUsedException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.POST_USED_BY_OTHER_ERROR;

    public WxPostUsedException() {
        super(WxPostUsedException.errorCode);
    }
}
