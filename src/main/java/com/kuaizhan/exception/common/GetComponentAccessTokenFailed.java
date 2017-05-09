package com.kuaizhan.exception.common;

/**
 * Created by zixiong on 2017/5/9.
 */
public class GetComponentAccessTokenFailed extends RuntimeException {
    public GetComponentAccessTokenFailed(String msg) {
        super(msg);
    }
    public GetComponentAccessTokenFailed(String msg, Throwable cause) {
        super(msg, cause);
    }
}
