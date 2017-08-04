package com.kuaizhan.kzweixin.dao.po.auto;

import com.kuaizhan.kzweixin.enums.MsgSendType;
import com.kuaizhan.kzweixin.enums.MsgType;

public class MsgPO {
    private Long msgId;

    private String appId;

    private String openId;

    private MsgType type;

    private String content;

    private MsgSendType sendType;

    private Boolean isCollect;

    private Integer status;

    private Integer createTime;

    private Integer updateTime;

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public MsgType getType() {
        return type;
    }

    public void setType(MsgType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MsgSendType getSendType() {
        return sendType;
    }

    public void setSendType(MsgSendType sendType) {
        this.sendType = sendType;
    }

    public Boolean getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(Boolean isCollect) {
        this.isCollect = isCollect;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }
}