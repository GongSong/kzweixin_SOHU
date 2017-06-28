package com.kuaizhan.kzweixin.exception;

import com.kuaizhan.kzweixin.constant.ErrorCode;

/**
 * 业务异常
 * 使用 unchecked RuntimeException, 可以外抛到最外层返回业务错误码。
 * Created by zixiong on 2017/5/1.
 */
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    private String message;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode =  errorCode;
        this.message = message;
    }

    public int getCode() {
        return errorCode.getCode();
    }

    public String getMessage() {
        return message;
    }
}
