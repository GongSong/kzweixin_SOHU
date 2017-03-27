package com.kuaizhan.exception.system;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * MQ消息异常
 * Created by zixiong on 2017/3/23.
 */
public class MqException extends SystemException{
    public MqException(Exception e) {
        super(ErrorCodeConfig.MQ_ERROR.getCode(), ErrorCodeConfig.MQ_ERROR.getMsg(), e);
    }
}
