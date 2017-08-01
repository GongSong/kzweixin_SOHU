package com.kuaizhan.kzweixin.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * 链接组类型
 * Created by zixiong on 2017/07/30.
 */
public enum LinkType {
    // 普通链接类型
    URL("url"),
    // 页面
    PAGE("page"),
    // 社区
    CLUB("club"),
    // 电商
    SHOP("shop"),
    // 海报
    POSTER("poster"),
    // 快文首页
    KW_MAIN("kw_home"),
    // 快文栏目
    KW_COLUMN("kw_column"),
    // 快文
    KW("kw_post");

    private String value;

    private static Map<String, LinkType> valueMap = new HashMap<>();

    static {
        for (LinkType type: LinkType.values()) {
            valueMap.put(type.getValue(), type);
        }
    }

    LinkType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static LinkType fromValue(String value) {
        return valueMap.get(value);
    }
}
