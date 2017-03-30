package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;

/**
 * 同步快站文章异常
 * Created by liangjiateng on 2017/3/30.
 */
public class KZPostAddException extends BusinessException{

    public KZPostAddException() {
        super(ErrorCodeConfig.ADD_KZ_POST_ERROR.getCode(), ErrorCodeConfig.ADD_KZ_POST_ERROR.getMsg());
    }
}
