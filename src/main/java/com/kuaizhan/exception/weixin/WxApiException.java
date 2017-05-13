package com.kuaizhan.exception.weixin;


/**
 * 微信APi调用失败异常
 * Created by zixiong on 2017/5/13.
 */
public class WxApiException extends RuntimeException {
    public WxApiException(String msg) {
        super(msg);
    }

    public WxApiException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
