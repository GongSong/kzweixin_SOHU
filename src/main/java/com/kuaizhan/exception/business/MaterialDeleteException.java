package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;

/**
 * 删除图文异常
 * Created by liangjiateng on 2017/3/28.
 */
public class MaterialDeleteException extends BusinessException {

    public MaterialDeleteException() {
        super(ErrorCodeConfig.DELETE_MATERIAL_ERROR.getCode(), ErrorCodeConfig.DELETE_MATERIAL_ERROR.getMsg());
    }
}
