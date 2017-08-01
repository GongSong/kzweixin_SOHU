package com.kuaizhan.kzweixin.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zixiong on 2017/08/01.
 */
public enum MsgSendType implements BaseEnum {
    TO_ACCOUNT(1), // 发给公众号
    TO_FAN(2); // 发给粉丝

    private int code;
    private static Map<Integer, MsgSendType> codeMap = new HashMap<>();
    MsgSendType(int code) {
        this.code = code;
    }

    static {
        for (MsgSendType type: MsgSendType.values()) {
            codeMap.put(type.getCode(), type);
        }
    }

    @Override
    public int getCode() {
        return code;
    }

    public static MsgSendType fromCode(int code) {
        return codeMap.get(code);
    }
}
