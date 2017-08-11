package com.kuaizhan.kzweixin.enums;


/**
 * Created by zixiong on 2017/6/26.
 */
public enum WxResponseType implements BaseEnum {
    TEXT(1),
    IMAGE(2),
    NEWS(3);

    private int value;

    WxResponseType(int value) {
        this.value = value;
    }

    public int getCode() {
        return this.value;
    }
}
