package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;

/**
 * Created by zixiong on 2017/4/27.
 */
public class ThumbMediaIdNotExistException extends BusinessException {
    public ThumbMediaIdNotExistException() {
        super(ErrorCodeConfig.THUMB_MEDIA_ID_NOT_EXIST_ERROR.getCode(), ErrorCodeConfig.THUMB_MEDIA_ID_NOT_EXIST_ERROR.getMsg());
    }

    public ThumbMediaIdNotExistException(String msg) {
        super(ErrorCodeConfig.THUMB_MEDIA_ID_NOT_EXIST_ERROR.getCode(), msg);
    }
}
