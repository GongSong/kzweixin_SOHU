package com.kuaizhan.kzweixin.dao.po.auto;

public class CustomMassPO {
    private Long customMassId;

    private Long weixinAppid;

    private Integer tagId;

    private Integer msgType;

    private Integer totalCount;

    private Integer successCount;

    private Integer rejectFailCount;

    private Integer otherFailCount;

    private Integer isTiming;

    private Integer publishTime;

    private Integer status;

    private Integer createTime;

    private Integer updateTime;

    private String msgJson;

    public Long getCustomMassId() {
        return customMassId;
    }

    public void setCustomMassId(Long customMassId) {
        this.customMassId = customMassId;
    }

    public Long getWeixinAppid() {
        return weixinAppid;
    }

    public void setWeixinAppid(Long weixinAppid) {
        this.weixinAppid = weixinAppid;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getRejectFailCount() {
        return rejectFailCount;
    }

    public void setRejectFailCount(Integer rejectFailCount) {
        this.rejectFailCount = rejectFailCount;
    }

    public Integer getOtherFailCount() {
        return otherFailCount;
    }

    public void setOtherFailCount(Integer otherFailCount) {
        this.otherFailCount = otherFailCount;
    }

    public Integer getIsTiming() {
        return isTiming;
    }

    public void setIsTiming(Integer isTiming) {
        this.isTiming = isTiming;
    }

    public Integer getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Integer publishTime) {
        this.publishTime = publishTime;
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

    public String getMsgJson() {
        return msgJson;
    }

    public void setMsgJson(String msgJson) {
        this.msgJson = msgJson;
    }

    /*
    * 兼容旧数据库的msg_type: 1文字 2微信图文 3图片 4链接组
    * */
    public enum MsgType {
        TEXT(1),
        ARTICLES(2),
        IMAGE(3),
        LINKS(4);

        private int code;

        MsgType(int code) {
            this.code = code;
        }
        public int getCode() {
            return code;
        }
    }

    /*
    * 兼容旧数据库的status: 0 删除，1 发送成功，2 发送失败，3 已发送，4 未发送
    * */
    public enum Status {
        DELETED(0),
        SUCCESS(1),
        FAILED(2),
        SENDED(3),
        UNSEND(4);
        private int code;

        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}