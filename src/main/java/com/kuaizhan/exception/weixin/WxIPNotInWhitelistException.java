package com.kuaizhan.exception.weixin;

/**
 * IP未被设置为白名单
 * Created by fangtianyu on 6/7/17.
 */
public class WxIPNotInWhitelistException extends RuntimeException{
    public WxIPNotInWhitelistException(String msg) {
        super(msg);
    }
}
