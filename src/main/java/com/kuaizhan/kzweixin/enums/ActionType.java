package com.kuaizhan.kzweixin.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zixiong on 2017/6/26.
 */
public enum ActionType {
    SUBSCRIBE(1),
    REPLY(2);

    private int value;
    private static Map<Integer, ActionType> valueMap = new HashMap<>();

    static {
        for (ActionType type: ActionType.values()) {
            valueMap.put(type.getValue(), type);
        }
    }

    ActionType(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return this.value;
    }

    @JsonCreator
    public static ActionType fromValue(int value) {
        return valueMap.get(value);
    }
}
