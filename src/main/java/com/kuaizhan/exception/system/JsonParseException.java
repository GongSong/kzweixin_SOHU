package com.kuaizhan.exception.system;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * json解析异常
 * Created by liangjiateng on 2017/3/15.
 */
public class JsonParseException extends SystemException {
    public JsonParseException(Exception e) {
        super(ErrorCodeConfig.JSON_PARSE_ERROR.getCode(), ErrorCodeConfig.JSON_PARSE_ERROR.getMsg(), e);
    }
}
