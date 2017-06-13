package com.kuaizhan.kzweixin.exception.kuaizhan;

/**
 * Created by zixiong on 2017/5/24.
 */
public class Export2KzException extends RuntimeException {

    public Export2KzException(String msg) {
        super(msg);
    }
    public Export2KzException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
