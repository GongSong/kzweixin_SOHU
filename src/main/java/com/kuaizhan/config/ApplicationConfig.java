package com.kuaizhan.config;

import com.kuaizhan.utils.PropertiesUtil;

import java.util.Properties;

/**
 * 应用配置
 * 读取自application.properties
 * Created by liangjiateng on 2017/3/1.
 */
public class ApplicationConfig {

    //资源读取工具
    private static final Properties PROP = PropertiesUtil.loadProps("application.properties");

    // 通用
    public static final String ENV_ALIAS = PropertiesUtil.getString(PROP, "env.alias");

    // redis前缀
    public static final String REDIS_PREFIX = PropertiesUtil.getString(PROP, "redis.key.prefix");
    // mongo前缀
    public static final String MONGO_PREFIX = PropertiesUtil.getString(PROP, "mongo.collection.prefix");

    // 快站
    public static final String KZ_SERVICE_HOST = PropertiesUtil.getString(PROP, "kz.service.host");
    public static final String KZ_SERVICE_IP = PropertiesUtil.getString(PROP, "kz.service.ip");
    public static final String KZ_PIC_HOST = PropertiesUtil.getString(PROP, "kz.pic.host");
    public static final String KZ_PIC_IP = PropertiesUtil.getString(PROP, "kz.pic.ip");
    public static final String KZ_PIC_REPLACE_HOST = PropertiesUtil.getString(PROP, "kz.pic.replaceHost");
    public static final String KZ_DOMAIN_RES = PropertiesUtil.getString(PROP, "kz.domain.res");

    // 分表
    public static final int MSG_TABLE_NUM = PropertiesUtil.getInt(PROP, "table.num.msg");
    public static final int FAN_TABLE_NUM = PropertiesUtil.getInt(PROP, "table.num.fan");

    //微信配置
    public static final String WEIXIN_TOKEN = PropertiesUtil.getString(PROP, "weixin.third.token");
    public static final String WEIXIN_APPID_THIRD = PropertiesUtil.getString(PROP, "weixin.third.appid");
    public static final String WEIXIN_APP_SECRET_THIRD = PropertiesUtil.getString(PROP, "weixin.third.appSecret");
    public static final String WEIXIN_AES_KEY = PropertiesUtil.getString(PROP, "weixin.third.aesKey");
}
