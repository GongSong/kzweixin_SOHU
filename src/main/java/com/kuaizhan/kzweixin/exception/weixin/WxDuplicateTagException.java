package com.kuaizhan.kzweixin.exception.weixin;

/**
 * 创建的新标签和原有标签重名
 * Created by fangtianyu on 6/15/17.
 */
public class WxDuplicateTagException extends RuntimeException {
    public WxDuplicateTagException(String msg) {
        super(msg);
    }
}
