package com.kuaizhan.kzweixin.exception.weixin;

/**
 * Created by fangtianyu on 6/21/17.
 */
public class WxOpenIdMismatchException extends RuntimeException {
    public WxOpenIdMismatchException(String msg) {
        super(msg);
    }
}