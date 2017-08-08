package com.kuaizhan.kzweixin.dao.po.auto;

import com.kuaizhan.kzweixin.enums.ComponentResponseType;

public class FollowReplyPO {
    private Integer id;

    private Long weixinAppid;

    private ComponentResponseType responseType;

    private Integer status;

    private Integer createTime;

    private Integer updateTime;

    private String responseJson;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getWeixinAppid() {
        return weixinAppid;
    }

    public void setWeixinAppid(Long weixinAppid) {
        this.weixinAppid = weixinAppid;
    }

    public ComponentResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ComponentResponseType responseType) {
        this.responseType = responseType;
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

    public String getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }
}