package com.kuaizhan.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用配置
 * Created by liangjiateng on 2017/3/1.
 */
public class ApplicationConfig {

    //接口前缀
    public static final String API_PREFIX = "http://94dbabe2de.test.kuaizhan.com/kzweixin";
    //大分页
    public static final int PAGE_SIZE_LARGE = 20;
    //版本号
    public static final String VERSION = "v1";

    //表数量
    public static final int TABLE_NUM_FAN = 2;

    public static List<String> getFanTableNames() {
        List<String> tables = new ArrayList<>();
        for (int i = 0; i < TABLE_NUM_FAN; i++) {
            tables.add("weixin_fans_" + i);
        }
        return tables;
    }

    public static final int TABLE_NUM_MSG = 2;

    public static List<String> getMsgTableNames() {
        List<String> tables = new ArrayList<>();
        for (int i = 0; i < TABLE_NUM_MSG; i++) {
            tables.add("weixin_msg_" + i);
        }
        return tables;
    }

    //智能回复

    public static final String YIYUAN_NEWS_APPID = "5774";

    public static final String YIYUAN_NEWS_SECRET = "daa62b37ee38403bb50b06382ce7d678";

    public static final String YIYUAN_JOKE_APPID = "5765";

    public static final String YIYUAN_JOKE_SECRET = "7deace10e42c49bea278c3ec11558217";

    public static final String YIYUAN_WEATHER_APPID = "5766";

    public static final String YIYUAN_WEATHER_APPSECRET = "12dcee7d371c4238b32997c4d8891615";

    public static final String YIYUAN_SONG_APPID = "5772";

    public static final String YIYUAN_SONG_SECRET = "642fb690593547338db18ace8c0f29a2";

    public static final String YIYUAN_STOCK_APPID = "16522";

    public static final String YIYUAN_STOCK_APPSECRET = "b4809796fe1645749ad011196d754b88";

    public static final String BAIDU_TRANSLATE_APPID = "20160304000014551";

    public static final String BAIDU_TRANSLATE_APPSECRET = "pLtcXFdLXVAf1DmNDOUg";

    //微信
    public static final String WEIXIN_TOKEN = "kuaizhan";

    public static final String WEIXIN_APPID_THIRD = "wxe07a6ac6045c62a7";

    public static final String WEIXIN_APPSECRECT_THIRD = "1eb08eded8d065ed55b2fb4be2841316";

    public static final String WEIXIN_AES_KEY = "1234567890123456789012345678901234567890123";

    //redis

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
    public static final String KEY_FAN_LIST = REDIS_PREFIX+"fan_list:";
}
