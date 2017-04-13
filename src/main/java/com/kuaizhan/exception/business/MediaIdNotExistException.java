package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;

/**
 * Created by zixiong on 2017/4/13.
 */
public class MediaIdNotExistException extends BusinessException{
    public MediaIdNotExistException() {
        super(ErrorCodeConfig.MEDIA_ID_NOT_EXIST_ERROR.getCode(), ErrorCodeConfig.MEDIA_ID_NOT_EXIST_ERROR.getMsg());
    }
}
