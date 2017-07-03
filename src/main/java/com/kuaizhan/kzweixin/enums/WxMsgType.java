package com.kuaizhan.kzweixin.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zixiong on 2017/5/18.
 */
public enum WxMsgType {

    TEXT("text"),
    IMAGE("image"),
    NEWS("news");

    private String value;

    private static Map<String, WxMsgType> valueMap = new HashMap<>();

    static {
        for (WxMsgType type: WxMsgType.values()) {
            valueMap.put(type.getValue(), type);
        }
    }

    WxMsgType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
    public static WxMsgType fromValue(String value) {
        return valueMap.get(value);
    }
}
