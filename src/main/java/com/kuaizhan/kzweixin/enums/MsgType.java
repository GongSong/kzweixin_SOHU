package com.kuaizhan.kzweixin.enums;

import java.util.HashMap;
import java.util.Map;

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
    MP_NEWS(9),
    LINK_GROUP(10),
    KEYWORD_TEXT(12),
    TPL_MSG(15);


    private int code;
    private static Map<Integer, MsgType> msgTypeMap = new HashMap<>();

    static {
        for (MsgType type: MsgType.values()) {
            msgTypeMap.put(type.getCode(), type);
        }
    }

    MsgType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
    public static MsgType fromValue(int code) {
        return msgTypeMap.get(code);
    }
}
