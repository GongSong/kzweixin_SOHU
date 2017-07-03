package com.kuaizhan.kzweixin.exception.weixin;

/**
 * Created by fangtianyu on 6/23/17.
 */
public class WxBlacklistExceedException extends RuntimeException {
    public WxBlacklistExceedException(String msg) {
        super(msg);
    }
}