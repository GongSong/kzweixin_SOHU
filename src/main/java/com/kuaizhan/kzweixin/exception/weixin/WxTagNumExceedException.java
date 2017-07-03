package com.kuaizhan.kzweixin.exception.weixin;

/**
 * 创建的新标签数量超过限制
 * Created by fangtianyu on 6/15/17.
 */
public class WxTagNumExceedException extends RuntimeException {
    public WxTagNumExceedException(String msg) {
        super(msg);
    }
}
