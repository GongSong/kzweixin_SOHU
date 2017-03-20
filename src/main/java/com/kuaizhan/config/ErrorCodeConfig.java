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
    SERVER_ERROR(100000, "服务器未知错误"),
    DATABASE_ERROR(100001, "数据库存储错误"),
    REDIS_ERROR(100002, "redis缓存错误"),
    JSON_PARSE_ERROR(100003, "JSON解析错误"),
    XML_PARSE_ERROR(100004, "XML解析错误"),
    ENCRYPT_ERROR(100005, "加密错误"),
    DECRYPT_ERROR(100006, "解密错误"),
    //账号(01)
    PARAM_ERROR(101001, "请求参数错误"),
    ACCOUNT_NULL_ERROR(101002, "公众号不存在或未绑定公众号"),

    //消息(02)
    SEND_CUSTOM_MSG_ERROR(102001, "发送客服消息失败"),

    //粉丝(03)
    GET_TAG_ERROR(103001, "获取标签失败，请重试"),
    TAG_DUPLICATE_NAME_ERROR(103002, "标签名非法或重复标签名"),
    TAG_NAME_LENGTH_ERROR(103003, "标签名不能超过30字节"),
    TAG_NUMBER_ERROR(103004, "创建的标签数过多，请注意不能超过100个"),
    OPEN_ID_NUMBER_ERROR(103005, "每次传入的openid列表个数不能超过50个"),
    FAN_TAG_NUMBER_ERROR(103006, "有粉丝身上的标签数已经超过限制"),
    OPEN_ID_ERROR(103007, "传入非法的openid"),
    TAG_ERROR(103008, "非法的标签"),
    TAG_MODIFY_ERROR(103009, "不能修改默认标签"),
    TAG_DELETE_FANS_NUMBER_ERROR(103010, "该标签下粉丝数超过10w，不允许直接删除"),
    BLACK_ADD_NUMBER_ERROR(103011, "一次只能拉黑20个用户");

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
