package com.kuaizhan.kzweixin.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zixiong on 2017/5/18.
 */
public enum WxMsgType {

    TEXT("text"),
    IMAGE("image"),
    VOICE("voice"),
    VIDEO("video"),
    SHORT_VIDEO("shortvideo"),
    LOCATION("location"),
    LINK("link"),
    NEWS("news"),

    MP_NEWS("mpnews"),
    MP_VIDEO("mpvideo"),
    WXCARD("wxcard");

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
