package com.kuaizhan.exception.weixin;

/**
 * 无效的templateId
 * Created by zixiong on 2017/5/13.
 */
public class WxInvalidTemplateException extends RuntimeException {
    public WxInvalidTemplateException(String msg) {
        super(msg);
    }
}
