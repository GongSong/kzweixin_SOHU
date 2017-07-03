package com.kuaizhan.kzweixin.exception.weixin;

/**
 * 微信接口，传参数据格式有误
 * Created by zixiong on 2017/5/13.
 */
public class WxDataFormatException extends RuntimeException {
    public WxDataFormatException(String msg) {
        super(msg);
    }
}
