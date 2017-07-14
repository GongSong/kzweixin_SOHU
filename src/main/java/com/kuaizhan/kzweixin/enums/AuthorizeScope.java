package com.kuaizhan.kzweixin.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zixiong on 2017/7/13.
 */
public enum  AuthorizeScope {
    SNSAPI_BASE("snsapi_base"), SNSAPI_USER_INFO("snsapi_userinfo");

    private static Map<String, AuthorizeScope> valueMap = new HashMap<>();
    private String value;

    static {
        for (AuthorizeScope scope: AuthorizeScope.values()) {
            valueMap.put(scope.getValue(), scope);
        }
    }

    AuthorizeScope(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AuthorizeScope fromValue(String value) {
        return valueMap.get(value);
    }
}
