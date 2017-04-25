package com.kuaizhan.constant;

import com.kuaizhan.config.ApplicationConfig;

/**
 * Redis相关常量
 * Created by zixiong on 2017/4/18.
 */
public class RedisConstant {

    //账户

    //缓存component_verify_ticket
    public static final String KEY_WEIXIN_COMPONENT_VERIFY_TICKET = ApplicationConfig.REDIS_PREFIX + "kz_weixin:ticket";
    //缓存component_access_token
    public static final String KEY_WEIXIN_COMPONENT_ACCESS_TOKEN = ApplicationConfig.REDIS_PREFIX + "kz_weixin:component_access_token";
    //缓存公众号的access_token
    public static final String KEY_WEIXIN_USER_ACCESS_TOKEN = ApplicationConfig.REDIS_PREFIX + "kz_weixin:user_access_token:";

    //缓存预授权码
    public static final String KEY_WEIXIN_PRE_AUTH_CODE = ApplicationConfig.REDIS_PREFIX + "pre_auth_code:";
    //缓存账户信息
    public static final String KEY_ACCOUNT_INFO = ApplicationConfig.REDIS_PREFIX + "account_info:";

    //粉丝

    //缓存粉丝列表
    public static final String KEY_FAN_LIST = ApplicationConfig.REDIS_PREFIX + "kzweixin:fan_list:";
    //缓存标签
    public static final String KEY_TAG = ApplicationConfig.REDIS_PREFIX + "kzweixin:tag:";

    //消息

    //缓存消息列表
    public static final String KEY_MSG_LIST = ApplicationConfig.REDIS_PREFIX + "kzweixin:msg_list:";
    //缓存单个用户消息
    public static final String KEY_MSG_USER = ApplicationConfig.REDIS_PREFIX + "kzweixin:msg_user:";

    // 上传过的图片资源
    public static final String KEY_IMAGE_WEIXIN_RUL = ApplicationConfig.REDIS_PREFIX + "kzweixin:origin_url:";

    // 图文模块
    public static final String KEY_COULD_SYNC_WX_POST = ApplicationConfig.REDIS_PREFIX + "kzweixin:could_sync_wx_post:";
    public static final int EXPIRE_COULD_SYNC_WX_POST = 10 * 60;
}
