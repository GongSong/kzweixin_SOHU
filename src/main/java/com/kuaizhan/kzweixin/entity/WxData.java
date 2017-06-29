package com.kuaizhan.kzweixin.entity;

import lombok.Data;

/**
 * 微信回调xml数据对象
 * Created by zixiong on 2017/6/28.
 */
@Data
public class WxData {

    private String appId;
    // 公众号name
    private String toUserName;
    // 用户name
    private String fromUserName;
    // 消息类型
    private String msgType;
    // 创建时间
    private String createTime;

    // 事件类型
    private String event;
    // 事件携带的key
    private String eventKey;
    // 消息text
    private String content;
}
