package com.kuaizhan.kzweixin.enums;

/**
 * Created by zixiong on 2017/08/01.
 */
public enum ComponentResponseType implements BaseEnum {

    POST(1), // 图文列表
    // 值为2的"页面"已经被废弃
    TEXT(3), // 文本
    IMAGE(4), // 图片
    LINK_GROUP(5); // 链接组

    private int value;

    ComponentResponseType(int value) {
        this.value = value;
    }

    @Override
    public int getCode() {
        return value;
    }
}
