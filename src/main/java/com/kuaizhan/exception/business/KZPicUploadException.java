package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;

/**
 * Created by lorin on 17-3-30.
 */
public class KZPicUploadException extends BusinessException {
    public KZPicUploadException() {
        super(ErrorCodeConfig.KZ_PIC_UPLOAD_ERROR.getCode(), ErrorCodeConfig.KZ_PIC_UPLOAD_ERROR.getMsg());
    }

    public KZPicUploadException(String msg) {
        super(ErrorCodeConfig.KZ_PIC_UPLOAD_ERROR.getCode(), msg);
    }
}
