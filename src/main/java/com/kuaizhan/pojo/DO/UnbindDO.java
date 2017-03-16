package com.kuaizhan.pojo.DO;

/**
 * 解绑信息
 * Created by Mr.Jadyn on 2017/1/25.
 */
public class UnbindDO {

    private Long weixinAppId;
    private Integer unbindType;
    private String unbindText;
    private Integer status;
    private Long createTime;
    private Long updateTime;

    public Long getWeixinAppId() {
        return weixinAppId;
    }

    public void setWeixinAppId(Long weixinAppId) {
        this.weixinAppId = weixinAppId;
    }

    public Integer getUnbindType() {
        return unbindType;
    }

    public void setUnbindType(Integer unbindType) {
        this.unbindType = unbindType;
    }

    public String getUnbindText() {
        return unbindText;
    }

    public void setUnbindText(String unbindText) {
        this.unbindText = unbindText;
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

    @Override
    public String toString() {
        return "Unbind{" +
                "weixinAppId=" + weixinAppId +
                ", unbindType=" + unbindType +
                ", unbindText='" + unbindText + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
