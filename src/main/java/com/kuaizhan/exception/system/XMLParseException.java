package com.kuaizhan.exception.system;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * xml解析异常
 * Created by liangjiateng on 2017/3/15.
 */
public class XMLParseException extends BaseException {
    public XMLParseException(String errorStack) {
        super(ErrorCodeConfig.XML_PARSE_ERROR.getCode(), ErrorCodeConfig.XML_PARSE_ERROR.getMsg(), errorStack);
    }
}
