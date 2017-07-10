package com.kuaizhan.kzweixin.constant;

/**
 * MQ的相关常量
 * 定义发送到MQ的routing key常量，这个值也和queue name一致
 * Created by zixiong on 2017/3/23.
 */
public class MqConstant {
    //从快站文章导入
    public static final String IMPORT_KUAIZHAN_POST = "sys-kzweixin-import-kuaizhan-post";
    //从微信文章导入列表
    public static final String IMPORT_WEIXIN_POST_LIST = "sys-kzweixin-import-weixin-post-list";
    //从微信文章导入
    public static final String IMPORT_WEIXIN_POST = "sys-kzweixin-import-weixin-post";
    // 发送模板消息
    public static final String SEND_SYS_TPL_MSG = "sys-kzweixin-send-sys-tpl-msg";
    // 用户绑定后的操作
    public static final String AFTER_BIND = "sys-kzweixin-after-bind";
    // 粉丝关注
    public static final String FAN_SUBSCRIBE = "sys-kzweixin-fan-subscribe";

    // 以下为php消息队列
    // 更新用户订阅
    public static final String WX_USER_SUBSCRIBE = "sys-weixin-user-subscribe.direct";
    // 用户取消订阅
    public static final String WX_USER_UNSUBSCRIBE = "sys-weixin-user-unsubscribe.direct";

}
