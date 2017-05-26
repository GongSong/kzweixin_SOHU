package com.kuaizhan.exception.weixin;

/**
 * Created by zixiong on 2017/5/26.
 */
public class WxRequireSubscribeException extends RuntimeException {
    public WxRequireSubscribeException(String msg) {
        super(msg);
    }
}
