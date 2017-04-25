package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;

/**
 * Created by zixiong on 2017/4/25.
 */
public class WxPostDeletedException extends BusinessException {
    public WxPostDeletedException() {
        super(ErrorCodeConfig.POST_DELETED_IN_WEIXIN.getCode(), ErrorCodeConfig.POST_DELETED_IN_WEIXIN.getMsg());
    }
}
