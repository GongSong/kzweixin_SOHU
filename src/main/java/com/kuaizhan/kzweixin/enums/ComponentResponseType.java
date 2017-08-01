package com.kuaizhan.kzweixin.enums;

/**
 * Created by zixiong on 2017/08/01.
 */
public enum ComponentResponseType implements BaseEnum {
    POST(1),
    PAGE(2),
    TEXT(3),
    IMAGE(4),
    LINK_GROUP(5);

    private int value;

    ComponentResponseType(int value) {
        this.value = value;
    }

    @Override
    public int getCode() {
        return value;
    }
}
