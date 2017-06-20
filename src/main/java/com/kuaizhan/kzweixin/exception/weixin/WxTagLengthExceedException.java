package com.kuaizhan.kzweixin.exception.weixin;

/**
 * 创建的新标签长度超过限制
 * Created by fangtianyu on 6/15/17.
 */
public class WxTagLengthExceedException extends RuntimeException {
    public WxTagLengthExceedException(String msg) {
        super(msg);
    }
}
