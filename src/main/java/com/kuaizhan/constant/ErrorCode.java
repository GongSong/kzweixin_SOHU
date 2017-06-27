package com.kuaizhan.constant;


import lombok.Data;

/**
 * 业务错误码
 * Created by zixiong on 2017/5/2.
 */
@Data
public class ErrorCode {

    //六位错误码，首位定位输出端（1为web端），中间两位定义模块，后三位定位具体错误类型
    //成功统一定义为200，系统(00)公共(01)账号(02)消息(03)粉丝(04)菜单(05)自动回复(06)图文(07)模板消息(08)

    public static final ErrorCode SUCCESS = new ErrorCode(200, "ok");

    // 系统级
    // 服务器未能捕捉到的异常, 异常抛到最外层被catch时，返回此错误码
    public static final ErrorCode SERVER_ERROR = new ErrorCode(100000, "服务器未知错误");
    // 用户操作导致的未知错误, 使用此业务异常，需要打印错误日志.
    public static final ErrorCode OPERATION_FAILED = new ErrorCode(100010, "操作失败");
    // TODO: 依赖第三方的未知错误定义

    // 通用(01)
    public static final ErrorCode PARAM_ERROR = new ErrorCode(101001, "请求参数错误");

    // 账号(02)
    public static final ErrorCode SITE_ID_NOT_EXIST = new ErrorCode(102002, "站点没有绑定公众号或siteId不存在");
    public static final ErrorCode ACCOUNT_NOT_EXIST = new ErrorCode(102003, "账号不存在");
    public static final ErrorCode ACCOUNT_NOT_VERIFIED_SERVICE_TYPE = new ErrorCode(102004, "账号不是认证的服务号");
    public static final ErrorCode APP_ID_NOT_EXIST = new ErrorCode(102005, "公众号不存在");
    public static final ErrorCode IP_NOT_IN_WHITELIST = new ErrorCode(102006, "IP未设置白名单");
    public static final ErrorCode INVALID_APP_SECRET = new ErrorCode(102007, "App Secret错误或App Secret不属于此公众号");

    // 图文(07)
    public static final ErrorCode POST_USED_BY_OTHER_ERROR = new ErrorCode(107001, "自定义菜单或自动回复中包含该图文，无法删除");
    public static final ErrorCode MEDIA_SIZE_OUT_OF_LIMIT = new ErrorCode(107002, "图片过大，不能上传到微信");
    public static final ErrorCode POST_NOT_EXIST_ERROR = new ErrorCode(107006, "图文id不存在");
    public static final ErrorCode SYNC_WX_POST_TOO_OFTEN_ERROR = new ErrorCode(107008, "10分钟内已经提交过同步请求，请稍后再试");
    public static final ErrorCode WX_POST_DELETED_ERROR = new ErrorCode(107009, "图文在微信后台被删除");
    public static final ErrorCode THUMB_MEDIA_ID_NOT_EXIST_ERROR = new ErrorCode(107010, "封面图在微信后台被删除，请重新上传");
    public static final ErrorCode DIFFERENT_POSTS_NUM_ERROR = new ErrorCode(107011, "多图文的条数在微信后台与快站不一致，请前往微信公众平台查看");
    public static final ErrorCode MEDIA_COUNT_OUT_OF_LIMIT = new ErrorCode(107012, "微信图片素材数量超出限制，请前往微信公众平台查看");
    public static final ErrorCode CAN_NOT_CHANGE_POST_NUM = new ErrorCode(107013, "由于微信限制，不能修改多图文的数目");

    // 消息、模板消息(08)
    public static final ErrorCode TPL_DATA_FORMAT_ERROR = new ErrorCode(108001, "模板消息数据格式错误");
    public static final ErrorCode INVALID_SYS_TEMPLATE_ID_ERROR = new ErrorCode(108002, "非法的系统模板消息id");
    public static final ErrorCode HAS_NOT_ADD_TEMPLATE_ERROR = new ErrorCode(108003, "公众号没有添加此模板消息id或已经被删除");
    public static final ErrorCode TEMPLATE_NUM_EXCEED_ERROR = new ErrorCode(108004, "公众号的模板消息数量已达上限");
    public static final ErrorCode TEMPLATE_INDUSTRY_CONFLICT_ERROR = new ErrorCode(108005, "公众号行业设置错误");
    public static final ErrorCode INVALID_OPEN_ID_ERROR = new ErrorCode(108006, "非法的openId或用户未关注公众号");


    private final int code;
    private final String message;
    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
