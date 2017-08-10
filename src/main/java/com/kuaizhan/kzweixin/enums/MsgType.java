package com.kuaizhan.kzweixin.enums;

/**
 * Created by zixiong on 2017/5/18.
 */
public enum MsgType implements BaseEnum {
    TEXT(1),
    IMAGE(2),
    VOICE(3),
    VIDEO(4),
    SHORT_VIDEO(5),
    LOCATION(6),
    LINK(7),
    POST(9),
    LINK_GROUP(10),
    KEYWORD_TEXT(12),
    TPL_MSG(15),
    RESERVE(99);
    private int code;

    MsgType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
