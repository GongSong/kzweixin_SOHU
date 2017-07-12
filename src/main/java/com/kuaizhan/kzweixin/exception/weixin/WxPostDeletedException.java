package com.kuaizhan.kzweixin.exception.weixin;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * Created by zixiong on 2017/7/12.
 */
public class WxPostDeletedException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.WX_POST_DELETED_ERROR;

    public WxPostDeletedException() {
        super(WxPostDeletedException.errorCode);
    }
}
