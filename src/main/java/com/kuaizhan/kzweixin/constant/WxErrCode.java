package com.kuaizhan.kzweixin.constant;

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
    public static final int INVALID_APP_SECRET_1 = 40001;  // 文档是这个
    public static final int INVALID_APP_SECRET_2 = 40125;  // 实际是这个
    // 调用接口的IP地址不在白名单中
    public static final int IP_NOT_IN_WHITELIST = 40164;

    // 创建失败，标签名已存在
    public static final int DUPLICATED_TAGS = 45157;
    // 标签名过长
    public static final int TAG_LENGTH_EXCEEDS = 45158;
    // 创建标签数过多
    public static final int TAG_NUM_EXCEEDS = 45056;
    // 不能修改0/1/2三个系统默认保留标签
    public static final int TAG_RESERVED_MODIFIED = 45058;
    // 标签下粉丝超过10万，不能直接删除标签
    public static final int TAG_FANS_EXCEED_10W = 45057;
    // 每次批量打标签不能超过50个OpenId
    public static final int OPEN_ID_EXCEED = 40032;
    // 非法标签
    public static final int INVALID_TAG = 45159;
    // 粉丝标签数超过20个的上限
    public static final int FANS_TAG_EXCEED = 45059;
    // OpenId不属于此AppId
    public static final int OPEN_ID_MISMATCH_APPID = 49003;
    // 单次拉黑用户超过20个上限
    public static final int ADD_BLACKLIST_EXCEED_LIMIT = 40032;

    // 图文被使用不能删除
    public static final int POST_USED_BY_OTHER = 48005;
    // 图文已经被删除
    public static final int POST_ALREADY_DELETED = 40007;
}
