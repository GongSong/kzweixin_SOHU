package com.kuaizhan.exception.system;
import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * Created by liangjiateng on 2017/3/24.
 */
public class MongoException extends SystemException {
    public MongoException(Exception e) {
        super(ErrorCodeConfig.MONGO_ERROR.getCode(), ErrorCodeConfig.MONGO_ERROR.getMsg(), e);
    }
}
