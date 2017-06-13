package com.kuaizhan.kzweixin.config;

/**
 * ################### 已废弃 ####################
 * ################### 待重构 ####################
 */

/**
 * Created by liangjiateng on 2017/3/1.
 */
public enum ErrorCodeConfig {

    //六位错误码，首位定位输出端（1为web端），中间两位定义模块，后三位定位具体错误类型
    //成功统一定义为200，系统(00)公共(01)账号(02)消息(03)粉丝(04)菜单(05)自动回复(06)图文(07)

    //系统级(00)
    SERVER_ERROR(100000, "服务器未知错误"),
    JSON_PARSE_ERROR(100003, "JSON解析错误"),
    ENCRYPT_ERROR(100005, "加密错误"),

    //公共(01)
    PARAM_ERROR(101001, "请求参数错误"),

    //消息(03)
    SEND_CUSTOM_MSG_ERROR(103001, "发送客服消息失败"),

    //粉丝(04)
    GET_TAG_ERROR(104001, "获取标签失败，请重试"),

    TAG_DUPLICATE_NAME_ERROR(104002, "标签名非法或重复标签名"),

    TAG_NAME_LENGTH_ERROR(104003, "标签名不能超过30字节"),

    TAG_NUMBER_ERROR(104004, "创建的标签数过多，请注意不能超过100个"),

    OPEN_ID_NUMBER_ERROR(104005, "每次传入的openid列表个数不能超过50个"),

    FAN_TAG_NUMBER_ERROR(104006, "有粉丝身上的标签数已经超过限制"),

    OPEN_ID_ERROR(104007, "传入非法的openid"),

    TAG_ERROR(104008, "非法的标签"),

    TAG_MODIFY_ERROR(104009, "不能修改默认标签"),

    TAG_DELETE_FANS_NUMBER_ERROR(104010, "该标签下粉丝数超过10w，不允许直接删除"),

    BLACK_ADD_NUMBER_ERROR(104011, "一次只能拉黑20个用户");

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
