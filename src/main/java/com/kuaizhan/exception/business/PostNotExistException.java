package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;

/**
 * Created by zixiong on 2017/3/31.
 */
public class PostNotExistException extends BusinessException{
    public PostNotExistException() {
        super(ErrorCodeConfig.POST_NOT_EXIST_ERROR.getCode(), ErrorCodeConfig.POST_NOT_EXIST_ERROR.getMsg());
    }
}
