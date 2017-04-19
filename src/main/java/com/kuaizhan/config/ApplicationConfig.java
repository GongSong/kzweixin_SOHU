package com.kuaizhan.config;

import com.kuaizhan.utils.PropertiesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 应用配置
 * 读取自application.properties
 * Created by liangjiateng on 2017/3/1.
 */
public class ApplicationConfig {

    //资源读取工具
    private static final Properties PROP = PropertiesUtil.loadProps("application.properties");

    public static final String REDIS_PREFIX = PropertiesUtil.getString(PROP, "redis.key.prefix");

    //版本号
    public static final String VERSION = "v1";
    //PHP项目版本号
    public static final String PHP_APP_VERSION = "5.0";

    //参数校验 json-schema
    //账户
    public static final String UNBIND_POSTDATAT_SCHEMA = "json-schema/account/unbind-postdata-schema.json";

    //分表
    private static int getMsgTableNum() {
        return PropertiesUtil.getInt(PROP, "table.num.msg");
    }

    public static List<String> getMsgTableNames() {
        List<String> tables = new ArrayList<>();
        for (int i = 0; i < getMsgTableNum(); i++) {
            tables.add("weixin_msg_" + i);
        }
        return tables;
    }

    public static String chooseMsgTable(long time) {
        return "weixin_msg_" + (time % getMsgTableNum());
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

    public static String chooseFanTable(long time) {
        return "weixin_fans_" + (time % getFanTableNum());
    }


    public static String getMongoCollectionPrefix() {
        return PropertiesUtil.getString(PROP, "mongo.collection.prefix");
    }

    //资源路径
    public static String getResUrl(String url) {
        return PropertiesUtil.getString(PROP, "domain.res") + url + "?v=" + PHP_APP_VERSION;
    }

    //图片上传路径
    public static String getPicUploadUrl() {
        return "http://" + PropertiesUtil.getString(PROP, "kz.service.ip") + "/pic/service-upload-pic-by-url";
    }

    public static String getPicHost() {
        return PropertiesUtil.getString(PROP, "kz.pic.host");
    }

    public static String getKzServiceHost() {
        return PropertiesUtil.getString(PROP, "kz.service.host");
    }

    public static String getPicReplaceHost(){
        return PropertiesUtil.getString(PROP, "kz.pic.replaceHost");
    }

    public static String getPicIp(){
        return PropertiesUtil.getString(PROP, "kz.pic.ip");
    }

    // 获取PHP端service host
    public static String getTestServiceHost() {
        return PropertiesUtil.getString(PROP, "kz.service.host");
    }

    //大分页
    public static final int PAGE_SIZE_LARGE = 20;
    //中分页
    public static final int PAGE_SIZE_MIDDLE = 10;
    //微信配置
    public static final String WEIXIN_TOKEN = PropertiesUtil.getString(PROP, "weixin.third.token");
    public static final String WEIXIN_APPID_THIRD = PropertiesUtil.getString(PROP, "weixin.third.appid");
    public static final String WEIXIN_APP_SECRET_THIRD = PropertiesUtil.getString(PROP, "weixin.third.appSecret");
    public static final String WEIXIN_AES_KEY = PropertiesUtil.getString(PROP, "weixin.third.aesKey");

}
