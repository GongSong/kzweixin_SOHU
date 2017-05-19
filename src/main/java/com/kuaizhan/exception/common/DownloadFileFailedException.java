package com.kuaizhan.exception.common;

/**
 * Created by zixiong on 2017/4/17.
 */
public class DownloadFileFailedException extends RuntimeException {
    public DownloadFileFailedException(String msg, Exception cause) {
        super(msg, cause);
    }
}
