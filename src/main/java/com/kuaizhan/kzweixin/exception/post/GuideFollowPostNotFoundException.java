package com.kuaizhan.kzweixin.exception.post;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * Created by zixiong on 2017/7/11.
 */
public class GuideFollowPostNotFoundException extends BusinessException {
    private static final ErrorCode errorCode = ErrorCode.GUIDE_FOLLOW_POST_NOT_FOUND;

    public GuideFollowPostNotFoundException() {
        super(GuideFollowPostNotFoundException.errorCode);
    }
}
