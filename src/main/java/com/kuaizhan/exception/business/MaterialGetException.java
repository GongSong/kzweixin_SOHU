package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;

/**
 * Created by lorin on 17-3-29.
 */
public class MaterialGetException extends BusinessException {
    public MaterialGetException(int code, String msg) {
        super(ErrorCodeConfig.GET_MATERIAL_ERROR.getCode(), ErrorCodeConfig.GET_MATERIAL_ERROR.getMsg());
    }
}
