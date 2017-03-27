package com.kuaizhan.exception.system;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * 数据存储异常
 * Created by liangjiateng on 2017/3/1.
 */
public class DaoException extends SystemException {

    public DaoException(Exception e) {
        super(ErrorCodeConfig.DATABASE_ERROR.getCode(), ErrorCodeConfig.DATABASE_ERROR.getMsg(), e);

    }

}
