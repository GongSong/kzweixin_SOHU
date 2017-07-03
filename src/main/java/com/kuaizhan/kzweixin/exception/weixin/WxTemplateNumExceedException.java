package com.kuaizhan.kzweixin.exception.weixin;

/**
 * 模板数量超出限制不能添加
 * Created by zixiong on 2017/5/15.
 */
public class WxTemplateNumExceedException extends RuntimeException {
    public WxTemplateNumExceedException() {
        super();
    }
    public WxTemplateNumExceedException(String msg) {
        super(msg);
    }
}
