package com.kuaizhan.config;

/**
 * 错误码
 * Created by liangjiateng on 2017/3/1.
 */
public enum ErrorCodeConfig {

    //六位错误码，首位定位输出端（1为web端），中间两位定义模块，后三位定位具体错误类型
    //成功统一定义为200，系统(00)公共(01)账号(02)消息(03)粉丝(04)菜单(05)自动回复(06)图文(07)

    //系统级(00)
    SUCCESS(200, "ok"),
    SERVER_ERROR(100000, "服务器未知错误"),
    DATABASE_ERROR(100001, "数据库存储错误"),
    REDIS_ERROR(100002, "redis缓存错误"),
    JSON_PARSE_ERROR(100003, "JSON解析错误"),
    XML_PARSE_ERROR(100004, "XML解析错误"),
    ENCRYPT_ERROR(100005, "加密错误"),
    DECRYPT_ERROR(100006, "解密错误"),
    MQ_ERROR(100007, "rabbitMQ错误"),
    MONGO_ERROR(100008, "mongo数据库错误"),

    //公共(01)
    PARAM_ERROR(101001, "请求参数错误"),
    DOWNLOAD_FILE_ERROR(101002, "图片下载失败"),

    //账号(02)
    ACCOUNT_NULL_ERROR(102002, "公众号不存在或未绑定公众号"),

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

    BLACK_ADD_NUMBER_ERROR(104011, "一次只能拉黑20个用户"),

    //图文(07)
    DELETE_MATERIAL_ERROR(107001, "删除图文失败"),

    ADD_MATERIAL_ERROR(107002, "新增素材失败"),

    GET_MATERIAL_ERROR(107003, "获取图文失败"),

    UPLOAD_POSTS_ERROR(107004, "上传微信多图文失败"),

    ADD_KZ_POST_ERROR(107005, "同步快站文章失败"),

    POST_NOT_EXIST_ERROR(107006, "图文id不存在"),

    MEDIA_ID_NOT_EXIST_ERROR(107007, "图文素材或者缩略图素材可能在微信后台被删除"),

    SYNC_WX_POST_TOO_OFTEN_ERROR(107008, "10分钟内已经提交过同步请求，请稍后再试。"),

    POST_DELETED_IN_WEIXIN(107009, "图文在微信后台被删除"),

    THUMB_MEDIA_ID_NOT_EXIST_ERROR(107010, "封面图在微信后台被删除，请重新上传"),

    WX_POST_LESS_THEN_POST(107011, "多图文的条数在微信后台与快站不一致，请前往微信公众平台查看。"),

    //KZPic(08)
    KZ_PIC_UPLOAD_ERROR(108001, "上传至KZPic失败");

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
