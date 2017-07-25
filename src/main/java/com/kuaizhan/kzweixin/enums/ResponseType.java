package com.kuaizhan.kzweixin.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zixiong on 2017/6/26.
 */
public enum ResponseType {
    TEXT(1),
    IMAGE(2),
    NEWS(3);

    private int value;
    private static Map<Integer, ResponseType> valueMap = new HashMap<>();

    static {
        for (ResponseType type: ResponseType.values()) {
            valueMap.put(type.getValue(), type);
        }
    }

    ResponseType(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return this.value;
    }

    @JsonCreator
    public static ResponseType fromValue(int value) {
        return valueMap.get(value);
    }
}
