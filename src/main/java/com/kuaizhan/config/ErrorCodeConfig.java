package com.kuaizhan.config;

/**
 * 错误码
 * Created by liangjiateng on 2017/3/1.
 */
public enum ErrorCodeConfig {

    //系统级
    SUCCESS(200, "ok"),
    DATABASE_ERROR(500, "数据库存储错误"),
    REDIS_ERROR(500, "redis缓存错误"),
    XML_PARSE_ERROR(500, "XML解析错误"),
    ENCRYPT_ERROR(500, "加密错误"),
    DECRYPT_ERROR(500, "解密错误"),
    JSON_PARSE_ERROR(500, "JSON解析错误"),

    //业务级
    PARAM_ERROR(406, "请求参数错误"),
    ACCOUNT_NULL_ERROR(404, "公众号不存在或未绑定公众号");

    private int code;
    private String msg;

    ErrorCodeConfig(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
