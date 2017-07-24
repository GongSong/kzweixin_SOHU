package com.kuaizhan.kzweixin.constant;

import com.kuaizhan.kzweixin.config.ApplicationConfig;

/**
 * Redis相关常量
 * kzweixin开头的是java重构后的key
 * kz_weixin开头的是兼容php的key
 * Created by zixiong on 2017/4/18.
 */
public class RedisConstant {

    // 账户

    // 缓存component_verify_ticket
    public static final String KEY_WEIXIN_COMPONENT_VERIFY_TICKET = ApplicationConfig.REDIS_PREFIX + "kz_weixin:ticket";
    // 缓存component_access_token
    public static final String KEY_WEIXIN_COMPONENT_ACCESS_TOKEN = ApplicationConfig.REDIS_PREFIX + "kz_weixin:component_access_token";
    // 缓存公众号的access_token
    public static final String KEY_WEIXIN_USER_ACCESS_TOKEN = ApplicationConfig.REDIS_PREFIX + "kz_weixin:user_access_token:";

    // 缓存账户信息
    public static final String KEY_ACCOUNT = ApplicationConfig.REDIS_PREFIX + "kzweixin:account:";
    public static final String KEY_ACCOUNT_BY_APPID = ApplicationConfig.REDIS_PREFIX + "kzweixin:account:appid:";

    // 粉丝

    // 缓存粉丝列表
    public static final String KEY_FAN_LIST = ApplicationConfig.REDIS_PREFIX + "kzweixin:fan_list:";
    // 缓存标签
    public static final String KEY_TAG = ApplicationConfig.REDIS_PREFIX + "kzweixin:tags:";
    // 缓存openId
    public static final String KEY_FAN_SUBSCRIBE_STATUS = ApplicationConfig.REDIS_PREFIX + "kz_weixin:app_id:open_id:";


    // 消息

    // 消息push token
    public static final String KEY_KZ_PUSH_TOKEN = ApplicationConfig.REDIS_PREFIX + "kz_weixin:msg_app_id:";


    // 上传过的图片资源
    public static final String KEY_IMAGE_WEIXIN_RUL = ApplicationConfig.REDIS_PREFIX + "kzweixin:origin_url:";
    public static final String KEY_V2_IMAGE_UPLOADED = ApplicationConfig.REDIS_PREFIX + "kzweixin:image_uploaded:";

    // 图文模块
    public static final String KEY_COULD_SYNC_WX_POST = ApplicationConfig.REDIS_PREFIX + "kzweixin:could_sync_wx_post:";
    public static final String KEY_GUIDE_FOLLOW_POST = ApplicationConfig.REDIS_PREFIX + "kzweixin:guide_follow_post:";

    // 授权登录
    public static final String KEY_AUTH_LOGIN_INFO = ApplicationConfig.REDIS_PREFIX + "kzweixin:auth_login_info:";


    /* ------------ php缓存key ------------*/
    public static final String KEY_PHP_ACCOUNT_BY_SITE_ID = ApplicationConfig.REDIS_PREFIX + "kz_weixin:plugin_by_site_id:";
}
