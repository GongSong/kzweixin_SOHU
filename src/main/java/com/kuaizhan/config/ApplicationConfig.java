package com.kuaizhan.config;

import com.kuaizhan.utils.PropertiesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 应用配置
 * Created by liangjiateng on 2017/3/1.
 */
public class ApplicationConfig {

    private static final Properties PROP = PropertiesUtil.loadProps("application.properties");
    //版本号
    public static final String VERSION = "v1";

    //分表
    private static int getMsgTableNum() {
        return PropertiesUtil.getInt(PROP, "table.num.msg");
    }

    private static int getFanTableNum() {
        return PropertiesUtil.getInt(PROP, "table.num.fan");
    }

    public static List<String> getFanTableNames() {
        List<String> tables = new ArrayList<>();
        for (int i = 0; i < getFanTableNum(); i++) {
            tables.add("weixin_fans_" + i);
        }
        return tables;
    }

    public static List<String> getMsgTableNames() {
        List<String> tables = new ArrayList<>();
        for (int i = 0; i < getMsgTableNum(); i++) {
            tables.add("weixin_msg_" + i);
        }
        return tables;
    }

    //接口前缀
    public static String getApiPrefix() {
        return PropertiesUtil.getString(PROP, "api.prefix");
    }


    //大分页
    public static final int PAGE_SIZE_LARGE = 20;

    //微信配置
    public static final String WEIXIN_TOKEN = "kuaizhan";


    public static final String WEIXIN_APPID_THIRD = "wxe07a6ac6045c62a7";


    public static final String WEIXIN_APPSECRECT_THIRD = "1eb08eded8d065ed55b2fb4be2841316";


    public static final String WEIXIN_AES_KEY = "1234567890123456789012345678901234567890123";



    //redis缓存前缀
    public static final String REDIS_PREFIX = "plf-dev-";

    //缓存component_verify_ticket
    public static final String KEY_WEIXIN_COMPONENT_VERIFY_TICKET = REDIS_PREFIX + "component_verify_ticket:";
    //缓存component_access_token
    public static final String KEY_WEIXIN_COMPONENT_ACCESS_TOKEN = REDIS_PREFIX + "component_access_token:";
    //缓存预授权码
    public static final String KEY_WEIXIN_PRE_AUTH_CODE = REDIS_PREFIX + "pre_auth_code:";
    //缓存账户信息
    public static final String KEY_ACCOUNT_INFO = REDIS_PREFIX + "account_info:";
    //缓存粉丝列表
    public static final String KEY_FAN_LIST = REDIS_PREFIX + "fan_list:";
}
