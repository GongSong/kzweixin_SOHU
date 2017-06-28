package com.kuaizhan.kzweixin.entity;

import lombok.Data;

/**
 * 微信回调xml数据对象
 * Created by zixiong on 2017/6/28.
 */
@Data
public class WxData {

    private String appId;
    private String toUserName;
    private String fromUserName;
    private String msgType;
    private String createTime;
    private String event;
    private String eventKey;
}
