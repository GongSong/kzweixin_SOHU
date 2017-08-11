package com.kuaizhan.kzweixin.dao.po.auto;

import com.kuaizhan.kzweixin.enums.ActionType;
import com.kuaizhan.kzweixin.enums.BizCode;
import com.kuaizhan.kzweixin.enums.WxResponseType;

public class ActionPO {
    private Integer id;

    private Long weixinAppid;

    private BizCode bizCode;

    private String keyword;

    private ActionType actionType;

    private WxResponseType responseType;

    private String responseJson;

    private String ext;

    private Boolean status;

    private Integer createTime;

    private Integer updateTime;

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

    public BizCode getBizCode() {
        return bizCode;
    }

    public void setBizCode(BizCode bizCode) {
        this.bizCode = bizCode;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public WxResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(WxResponseType responseType) {
        this.responseType = responseType;
    }

    public String getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
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