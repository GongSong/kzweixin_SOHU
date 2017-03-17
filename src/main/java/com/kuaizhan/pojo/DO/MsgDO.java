package com.kuaizhan.pojo.DO;


/**
 * 消息
 * Created by Mr.Jadyn on 2017/1/10.
 */
public class MsgDO {

    private Long msgId;
    private String appId;
    private String openId;
    private Integer type;
    private String content;
    private Integer sendType;
    private Integer isCollect;
    private Integer status;
    private Integer isFoucs;
    private Long createTime;
    private Long updateTime;
    private String headImgUrl;
    private String nickName;


    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
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

    public Integer getIsFoucs() {
        return isFoucs;
    }

    public void setIsFoucs(Integer isFoucs) {
        this.isFoucs = isFoucs;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Integer getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(Integer isCollect) {
        this.isCollect = isCollect;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "msgId=" + msgId +
                ", appId='" + appId + '\'' +
                ", openId='" + openId + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", sendType=" + sendType +
                ", isCollect=" + isCollect +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }

}
