package com.kuaizhan.utils;

import com.kuaizhan.config.ApplicationConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zixiong on 2017/4/25.
 */
public class DBTableUtil {

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
