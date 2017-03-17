package com.kuaizhan.config;

/**
 * 错误码
 * Created by liangjiateng on 2017/3/1.
 */
public enum ErrorCodeConfig {

    //六位错误码，首位定位输出端（1为web端），中间两位定义模块，后三位定位具体错误类型
    //成功统一定义为200，系统(00)账号(01)消息(02)粉丝(03)菜单(04)自动回复(05)图文(06)

    //系统级(00)
    SUCCESS(200, "ok"),
    DATABASE_ERROR(100001, "数据库存储错误"),
    REDIS_ERROR(100002, "redis缓存错误"),
    JSON_PARSE_ERROR(100003, "JSON解析错误"),
    XML_PARSE_ERROR(100004, "XML解析错误"),
    ENCRYPT_ERROR(100005, "加密错误"),
    DECRYPT_ERROR(100006, "解密错误"),
    //账号(01)
    PARAM_ERROR(101001, "请求参数错误"),
    ACCOUNT_NULL_ERROR(101002, "公众号不存在或未绑定公众号");

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
