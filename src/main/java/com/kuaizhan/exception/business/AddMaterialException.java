package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;

/**
 * 新增素材异常
 * Created by liangjiateng on 2017/3/29.
 */
public class AddMaterialException extends BusinessException {
    public AddMaterialException() {
        super(ErrorCodeConfig.ADD_MATERIAL_ERROR.getCode(), ErrorCodeConfig.ADD_MATERIAL_ERROR.getMsg());
    }
}
