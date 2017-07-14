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
}