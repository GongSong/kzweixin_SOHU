package com.kuaizhan.pojo.DO;

/**
 * 账户数据对象
 * Created by liangjiateng on 2017/3/15.
 */
public class AccountDO {

    private Long weixinAppId;
    private Long siteId;
    private String accessToken;
    private String refreshToken;
    private Long expiresTime;
    private String appId;
    private String appSecret;
    private String funcInfoJson;
    private String nickName;
    private String headImg;
    private Integer serviceType;


    private Integer verifyType;
    private String userName;
    private String alias;
    private String businessInfoJson;
    private String qrcodeUrl;
    private String qrcodeUrlKz;
    private String menuJson;
    private String interestJson;
    private String advancedFuncInfoJson;
    private String previewOpenId;
    private Long bindTime;
    private Long unbindTime;
    private Integer isDel;
    private Long createTime;
    private Long updateTime;


    public Long getWeixinAppId() {
        return weixinAppId;
    }

    public void setWeixinAppId(Long weixinAppId) {
        this.weixinAppId = weixinAppId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getBindTime() {
        return bindTime;
    }

    public void setBindTime(Long bindTime) {
        this.bindTime = bindTime;
    }

    public Long getUnbindTime() {
        return unbindTime;
    }

    public void setUnbindTime(Long unbindTime) {
        this.unbindTime = unbindTime;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(Long expiresTime) {
        this.expiresTime = expiresTime;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getFuncInfoJson() {
        return funcInfoJson;
    }

    public void setFuncInfoJson(String funcInfoJson) {
        this.funcInfoJson = funcInfoJson;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(Integer verifyType) {
        this.verifyType = verifyType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getBusinessInfoJson() {
        return businessInfoJson;
    }

    public void setBusinessInfoJson(String businessInfoJson) {
        this.businessInfoJson = businessInfoJson;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    public String getQrcodeUrlKz() {
        return qrcodeUrlKz;
    }

    public void setQrcodeUrlKz(String qrcodeUrlKz) {
        this.qrcodeUrlKz = qrcodeUrlKz;
    }

    public String getMenuJson() {
        return menuJson;
    }

    public void setMenuJson(String menuJson) {
        this.menuJson = menuJson;
    }

    public String getInterestJson() {
        return interestJson;
    }

    public void setInterestJson(String interestJson) {
        this.interestJson = interestJson;
    }

    public String getAdvancedFuncInfoJson() {
        return advancedFuncInfoJson;
    }

    public void setAdvancedFuncInfoJson(String advancedFuncInfoJson) {
        this.advancedFuncInfoJson = advancedFuncInfoJson;
    }

    public String getPreviewOpenId() {
        return previewOpenId;
    }

    public void setPreviewOpenId(String previewOpenId) {
        this.previewOpenId = previewOpenId;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    @Override
    public String toString() {
        return "Account{" +
                "weixinAppId=" + weixinAppId +
                ", siteId=" + siteId +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiresTime=" + expiresTime +
                ", appId='" + appId + '\'' +
                ", appSecret='" + appSecret + '\'' +
                ", funcInfoJson='" + funcInfoJson + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headImg='" + headImg + '\'' +
                ", serviceType=" + serviceType +
                ", verifyType=" + verifyType +
                ", userName='" + userName + '\'' +
                ", alias='" + alias + '\'' +
                ", businessInfoJson='" + businessInfoJson + '\'' +
                ", qrcodeUrl='" + qrcodeUrl + '\'' +
                ", qrcodeUrlKz='" + qrcodeUrlKz + '\'' +
                ", menuJson='" + menuJson + '\'' +
                ", interestJson='" + interestJson + '\'' +
                ", advancedFuncInfoJson='" + advancedFuncInfoJson + '\'' +
                ", previewOpenId='" + previewOpenId + '\'' +
                ", bindTime=" + bindTime +
                ", unbindTime=" + unbindTime +
                ", isDel=" + isDel +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
