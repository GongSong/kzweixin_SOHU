package com.kuaizhan.kzweixin.enums;

/**
 * Created by zixiong on 2017/6/26.
 */
public enum ActionType implements BaseEnum {
    SUBSCRIBE(1),
    REPLY(2);

    private int code;

    ActionType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
