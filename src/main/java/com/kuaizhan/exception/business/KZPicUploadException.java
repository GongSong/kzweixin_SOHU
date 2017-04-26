package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * Created by lorin on 17-3-30.
 */
public class KZPicUploadException extends BusinessException {
    public KZPicUploadException() {
        super(ErrorCodeConfig.KZPIC_UPLOAD_ERROR.getCode(), ErrorCodeConfig.KZPIC_UPLOAD_ERROR.getMsg());
    }

    public KZPicUploadException(String msg) {
        super(ErrorCodeConfig.KZPIC_UPLOAD_ERROR.getCode(), msg);
    }
}
