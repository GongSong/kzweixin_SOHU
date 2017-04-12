package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * 发送客服消息异常
 * Created by liangjiateng on 2017/3/20.
 */
public class SendCustomMsgException extends BusinessException {
    public SendCustomMsgException() {
        super(ErrorCodeConfig.SEND_CUSTOM_MSG_ERROR.getCode(), ErrorCodeConfig.SEND_CUSTOM_MSG_ERROR.getMsg());
    }
}
