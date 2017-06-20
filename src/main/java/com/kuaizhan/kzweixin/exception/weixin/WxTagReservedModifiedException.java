package com.kuaizhan.kzweixin.exception.weixin;

/**
 * Created by fangtianyu on 6/16/17.
 */
public class WxTagReservedModifiedException extends RuntimeException {
    public WxTagReservedModifiedException(String msg) {
        super(msg);
    }
}
