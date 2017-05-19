package com.kuaizhan.exception.weixin;

/**
 * Created by zixiong on 2017/5/18.
 */
public class WxInvalidOpenIdException extends RuntimeException {
    public WxInvalidOpenIdException(String msg) {
        super(msg);
    }
}
