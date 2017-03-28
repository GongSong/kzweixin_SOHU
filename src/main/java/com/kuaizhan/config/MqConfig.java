package com.kuaizhan.config;

/**
 * MQ的config类
 * 定义发送到MQ的routing key常量，这个值也和queue name一致
 * Created by zixiong on 2017/3/23.
 */
public class MqConfig {
    public static final String IMPORT_KUAIZHAN_POST = "sys-weixin-import-kuaizhan-post";
    public static final String TEST = "sys-weixin-test";
}
