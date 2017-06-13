package com.kuaizhan.kzweixin.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zixiong on 2017/5/18.
 */
public enum MsgType {
    TEXT((short) 1),
    IMAGE((short) 2),
    VOICE((short) 3),
    VIDEO((short) 4),
    MP_NEWS((short) 9),
    NEWS((short) 10),
    KEYWORD_TEXT((short) 12),
    TPL_MSG((short) 15);


    private short value;
    private static Map<Short, MsgType> msgTypeMap = new HashMap<>();

    static {
        for (MsgType type: MsgType.values()) {
            msgTypeMap.put(type.getValue(), type);
        }
    }

    MsgType(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }
    public static MsgType fromValue(short value) {
        return msgTypeMap.get(value);
    }
}
