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
    // 用户openId，和fromUserName是同一回事
    private String openId;
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
    // 消息Id
    private String msgId;
    // 用户状态
    private String status;
    // 群发总数
    private String totalCount;
    // 过滤器总数
    private String filterCount;
    // 已成功群发总数
    private String sentCount;
    // 群发失败总数
    private String errorCount;
}
