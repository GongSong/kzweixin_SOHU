package com.kuaizhan.exception;

/**
 * 系统级异常
 * Created by liangjiateng on 2017/3/1.
 */
public abstract class BaseException extends Exception {
    /**
     * 异常发生时间
     */
    private long date = System.currentTimeMillis();

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 错误栈
     */
    private String errorStack;


    public BaseException(int code, String msg, String errorStack) {
        this.code = code;
        this.msg = msg;
        this.errorStack = errorStack;
    }

    public String getErrorStack() {
        return errorStack;
    }

    public long getDate() {
        return date;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
