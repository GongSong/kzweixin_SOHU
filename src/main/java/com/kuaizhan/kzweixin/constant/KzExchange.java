package com.kuaizhan.kzweixin.constant;

/**
 * php代码中，rabbitMq的exchange常量
 * Created by zixiong on 2017/6/9.
 */
public class KzExchange {
    public static final String SETTINGS_IMPORT = "sys-weixin-settings-import.direct";
    public static final String OTHER_AFTER_FIRST_BIND = "sys-weixin-other-after-first-bind.direct";
    public static final String USER_IMPORT = "sys-weixin-user-import.direct";
    public static final String GUIDE_FOLLOW_ADD = "sys-weixin-guide-follow-add.direct";
    public static final String OTHER_SETTINGS_AFTER_BIND = "sys-weixin-other-settings-after-bind.direct";
    public static final String ADD_MSG = "sys-weixin-msg-add.direct";

    // 统计
    public static final String KZ_STAT_INC = "sys-kz-stat-inc.direct";
}
