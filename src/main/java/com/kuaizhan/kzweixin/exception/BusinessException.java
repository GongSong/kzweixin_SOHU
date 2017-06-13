package com.kuaizhan.kzweixin.exception;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import lombok.Data;

/**
 * 业务异常
 * 业务异常属于不可恢复异常，使用 unchecked RuntimeException, 外抛到最外层返回错误码。
 * Created by zixiong on 2017/5/1.
 */
@Data
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

    public int getCode() {
        return this.errorCode.getCode();
    }
}
