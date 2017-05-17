package com.kuaizhan.pojo.vo;

import lombok.Data;

import java.util.Map;

/**
 * 消息展示对象
 * Created by liangjiateng on 2017/3/18.
 */
@Data
public class MsgVO {
    private int msgType;
    private int sendType;
    private String nickname;
    private String headImgUrl;
    private String openId;
    private Map content;
    private Long createTime;
}
