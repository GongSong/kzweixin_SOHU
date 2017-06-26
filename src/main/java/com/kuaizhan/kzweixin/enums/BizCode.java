package com.kuaizhan.kzweixin.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zixiong on 2017/6/26.
 */
public enum BizCode {
    VOTE("vote");

    private String value;
    private static Map<String, BizCode> valueMap = new HashMap<>();

    static {
        for (BizCode bizCode: BizCode.values()) {
            valueMap.put(bizCode.getValue(), bizCode);
        }
    }

    BizCode(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static BizCode fromValue(String value) {
        return valueMap.get(value);
    }
}
