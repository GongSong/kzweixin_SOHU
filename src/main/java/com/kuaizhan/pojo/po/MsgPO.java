package com.kuaizhan.pojo.po;


import lombok.Data;

/**
 * 消息
 * Created by Mr.Jadyn on 2017/1/10.
 */
@Data
public class MsgPO {

    private Long msgId;
    private String appId;
    private String openId;
    private Integer type;
    private String content;
    private Integer sendType;
    private Integer isCollect;
    private Integer status;
    private Integer isFocus;
    private Long createTime;
    private Long updateTime;
    private String headImgUrl;
    private String nickName;
}
