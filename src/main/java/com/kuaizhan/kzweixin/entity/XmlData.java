package com.kuaizhan.kzweixin.entity;

import lombok.Data;
import org.dom4j.Element;

/**
 * 微信回调xml数据对象
 * Created by zixiong on 2017/6/28.
 */
@Data
public class XmlData {

    /**
     * 必有字段
     */
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

    /**
     * 原始的xml数据
     */
    private Element xmlRoot;

}