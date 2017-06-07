package com.kuaizhan.mq.dto;

import lombok.Data;

import java.util.Map;

/**
 * Created by zixiong on 2017/6/7.
 */
@Data
public class SendTplMsgDTO {
    private Long weixinAppid;
    private String tplId;
    private String tplIdShort;
    private String openId;
    private String url;
    private Map dataMap;
}
