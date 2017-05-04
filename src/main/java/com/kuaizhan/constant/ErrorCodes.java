package com.kuaizhan.constant;

import com.kuaizhan.common.ErrorCode;

/**
 * 业务错误码
 * Created by zixiong on 2017/5/2.
 */
public class ErrorCodes {

    // 系统级
    // 用户操作导致的未知错误, 使用此业务异常，需要打印错误日志.
    public static final ErrorCode OPERATION_FAILED = new ErrorCode(100010, "操作失败");
    // TODO: 依赖第三方的未知错误定义

    // 图文(07)
    public static final ErrorCode POST_USED_BY_OTHER_ERROR = new ErrorCode(107001, "自定义菜单或自动回复中包含该图文，无法删除");
    public static final ErrorCode MEDIA_SIZE_OUT_OF_LIMIT = new ErrorCode(107002, "图片过大，不能上传到微信");
    public static final ErrorCode POST_NOT_EXIST_ERROR = new ErrorCode(107006, "图文id不存在");
    public static final ErrorCode SYNC_WX_POST_TOO_OFTEN_ERROR = new ErrorCode(107008, "10分钟内已经提交过同步请求，请稍后再试");
    public static final ErrorCode WX_POST_DELETED_ERROR = new ErrorCode(107009, "图文在微信后台被删除");
    public static final ErrorCode THUMB_MEDIA_ID_NOT_EXIST_ERROR = new ErrorCode(107010, "封面图在微信后台被删除，请重新上传");
    public static final ErrorCode DIFFERENT_POSTS_NUM_ERROR = new ErrorCode(107011, "多图文的条数在微信后台与快站不一致，请前往微信公众平台查看");
}
