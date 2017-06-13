package com.kuaizhan.kzweixin.utils;

import com.kuaizhan.kzweixin.config.ApplicationConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zixiong on 2017/4/25.
 */
public class DBTableUtil {

    public static String getMsgTableName(String appId) {
        int tableNum = (int) (Crc32Util.getValue(appId) % ApplicationConfig.MSG_TABLE_NUM);
        return "weixin_msg_" + tableNum;
    }

    public static String getFanTableName(String appId) {
        int tableNum = (int) (Crc32Util.getValue(appId) % ApplicationConfig.FAN_TABLE_NUM);
        return "weixin_fans_" + tableNum;
    }

    public static List<String> getMsgTableNames() {
        List<String> tables = new ArrayList<>();
        for (int i = 0; i < ApplicationConfig.MSG_TABLE_NUM; i++) {
            tables.add("weixin_msg_" + i);
        }
        return tables;
    }

    public static String chooseMsgTable(long time) {
        return "weixin_msg_" + (time % ApplicationConfig.MSG_TABLE_NUM);
    }


    public static List<String> getFanTableNames() {
        List<String> tables = new ArrayList<>();
        for (int i = 0; i < ApplicationConfig.FAN_TABLE_NUM; i++) {
            tables.add("weixin_fans_" + i);
        }
        return tables;
    }

    public static String chooseFanTable(long time) {
        return "weixin_fans_" + (time % ApplicationConfig.FAN_TABLE_NUM);
    }
}
