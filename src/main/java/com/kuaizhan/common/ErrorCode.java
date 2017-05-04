package com.kuaizhan.common;

import lombok.Data;

/**
 * 业务异常的错误码类
 * Created by zixiong on 2017/5/2.
 */

@Data
public class ErrorCode {

    private final int code;
    private String message;

    public ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
