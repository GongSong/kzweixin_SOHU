package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;

/**
 * Created by zixiong on 2017/4/19.
 */
public class SyncWXPostTooOftenException extends BusinessException {
    public SyncWXPostTooOftenException(){
        super(ErrorCodeConfig.SYNC_WX_POST_TOO_OFTEN_ERROR.getCode(), ErrorCodeConfig.SYNC_WX_POST_TOO_OFTEN_ERROR.getMsg());
    }
}
