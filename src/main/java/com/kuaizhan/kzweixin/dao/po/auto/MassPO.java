package com.kuaizhan.kzweixin.dao.po.auto;

import java.util.HashMap;
import java.util.Map;

public class MassPO {
    private Long massId;

    private Long weixinAppid;

    private Integer responseType;

    private String msgId;

    private String statusMsg;

    private Integer totalCount;

    private Integer filterCount;

    private Integer sentCount;

    private Integer errorCount;

    private Integer groupId;

    private Integer isTiming;

    private Long publishTime;

    private Integer status;

    private Long createTime;

    private Long updateTime;

    private String responseJson;

    public Long getMassId() {
        return massId;
    }

    public void setMassId(Long massId) {
        this.massId = massId;
    }

    public Long getWeixinAppid() {
        return weixinAppid;
    }

    public void setWeixinAppid(Long weixinAppid) {
        this.weixinAppid = weixinAppid;
    }

    public Integer getResponseType() {
        return responseType;
    }

    public void setResponseType(Integer responseType) {
        this.responseType = responseType;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getFilterCount() {
        return filterCount;
    }

    public void setFilterCount(Integer filterCount) {
        this.filterCount = filterCount;
    }

    public Integer getSentCount() {
        return sentCount;
    }

    public void setSentCount(Integer sentCount) {
        this.sentCount = sentCount;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getIsTiming() {
        return isTiming;
    }

    public void setIsTiming(Integer isTiming) {
        this.isTiming = isTiming;
    }

    public Long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Long publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }

    /*
    * 兼容旧数据库的response_type: 1 文章列表，2 页面，3 文字，4 图片
    * */
    public enum RespType {
        ARTICLES(1),
        PAGE(2),
        TEXT(3),
        IMAGE(4);

        private int code;

        private static Map<Integer, RespType> respTypeMap = new HashMap<>();

        static {
            for(RespType t : RespType.values()) {
                respTypeMap.put(t.getCode(), t);
            }
        }

        RespType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static RespType fromValue(int code) {
            return respTypeMap.get(code);
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