package com.kuaizhan.constant;

/**
 * Created by zixiong on 2017/5/13.
 */
public class WxErrCode {
    // 无效模板id
    public static final int INVALID_TEMPLATE_ID = 40037;
    // 模板消息数量达到上限
    public static final int TEMPLATE_NUM_EXCEEDS_LIMIT = 45026;
    // 添加的模板与行业设置冲突
    public static final int TEMPLATE_INDUSTRY_CONFLICT = 45027;
    // 非法的行业id，（出现的场景还不明确）
    public static final int INVALID_INDUSTRY_ID = 40102;
    // 数据格式不符合要求
    public static final int DATA_FORMAT_ERROR = 47001;
    // 无效的openId
    public static final int INVALID_OPEN_ID = 40003;
    // 回复次数超出限制
    public static final int OUT_OF_RESPONSE_LIMIT = 45047;

    // 图片太大
    public static final int MEDIA_SIZE_OUT_OF_LIMIT = 45001;
    // 图片格式不对
    public static final int INVALID_IMAGE_FORMAT = 40137;
    // 永久素材数量超过限制
    public static final int MEDIA_COUNT_OUT_OF_LIMIT = 45034;

    // 需要关注
    public static final int REQUIRE_SUBSCRIBE = 43004;

    // App Secret错误或App Secret不属于此公众号
    public static final int INVALID_APP_SECRET = 40001;
    // 调用接口的IP地址不在白名单中
    public static final int IP_NOT_IN_WHITELIST = 40164;
}
