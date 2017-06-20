package com.kuaizhan.kzweixin.constant;

import com.kuaizhan.kzweixin.config.ApplicationConfig;

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

    //缓存账户信息
    public static final String KEY_ACCOUNT_INFO = ApplicationConfig.REDIS_PREFIX + "account_info:";

    //粉丝

    //缓存粉丝列表
    public static final String KEY_FAN_LIST = ApplicationConfig.REDIS_PREFIX + "kzweixin:fan_list:";
    //缓存标签
    public static final String KEY_TAG = ApplicationConfig.REDIS_PREFIX + "kzweixin:tags:";

    //消息

    // 消息push token
    public static final String KEY_KZ_PUSH_TOKEN = ApplicationConfig.REDIS_PREFIX + "kz_weixin:msg_app_id:";


    // 上传过的图片资源
    public static final String KEY_IMAGE_WEIXIN_RUL = ApplicationConfig.REDIS_PREFIX + "kzweixin:origin_url:";

    // 图文模块
    public static final String KEY_COULD_SYNC_WX_POST = ApplicationConfig.REDIS_PREFIX + "kzweixin:could_sync_wx_post:";
    public static final int EXPIRE_COULD_SYNC_WX_POST = 10 * 60;
}
