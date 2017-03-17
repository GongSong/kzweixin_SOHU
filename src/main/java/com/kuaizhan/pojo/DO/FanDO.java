package com.kuaizhan.pojo.DO;


/**
 * 粉丝model
 * Created by Mr.Jadyn on 2017/1/4.
 */
public class FanDO {


    private Long fanId;
    private String appId;
    private String openId;
    private String nickName;
    private Integer sex;
    private String city;
    private String province;
    private String country;
    private String language;
    private String headImgUrl;
    private Long subscribeTime;
    private String unionId;
    private String remark;
    private Long groupId;
    private String tagIdsJson;

    private Integer inBlackList;
    private Long lastInteractTime;
    private Integer status;
    private Long createTime;
    private Long updateTime;

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getInBlackList() {
        return inBlackList;
    }

    public void setInBlackList(Integer inBlackList) {
        this.inBlackList = inBlackList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public long getFanId() {
        return fanId;
    }

    public void setFanId(long fanId) {
        this.fanId = fanId;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }


    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }


    public String getTagIdsJson() {
        return tagIdsJson;
    }

    public void setTagIdsJson(String tagIdsJson) {
        this.tagIdsJson = tagIdsJson;
    }



    public long getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(long subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public long getLastInteractTime() {
        return lastInteractTime;
    }

    public void setLastInteractTime(long lastInteractTime) {
        this.lastInteractTime = lastInteractTime;
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
        return "Fans{" +
                "fanId=" + fanId +
                ", appId='" + appId + '\'' +
                ", openId='" + openId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", sex=" + sex +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", language='" + language + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", subscribeTime=" + subscribeTime +
                ", unionId='" + unionId + '\'' +
                ", remark='" + remark + '\'' +
                ", groupId=" + groupId +
                ", tagIdsJson='" + tagIdsJson + '\'' +
                ", inBlackList=" + inBlackList +
                ", lastInteractTime=" + lastInteractTime +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}
