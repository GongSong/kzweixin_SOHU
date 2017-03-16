package com.kuaizhan.pojo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 公众号帐号基本信息业务对象
 * Created by liangjiateng on 2017/3/15.
 */
public class AuthorizerInfoDTO {
    @JsonProperty("nick_name")
    private String nickName;
    @JsonProperty("head_img")
    private String headImg;
    @JsonProperty("service_type_info")
    private String serviceTypeInfo;
    @JsonProperty("verify_type_info")
    private String verifyTypeInfo;
    @JsonProperty("user_name")
    private String username;
    @JsonProperty("principal_name")
    private String principalName;
    @JsonProperty("business_info")
    private String businessInfo;
    @JsonProperty("alias")
    private String alias;
    @JsonProperty("qrcode_url")
    private String qrcodeUrl;
    @JsonProperty("authorization_info")
    private AuthorizationInfoDTO authorizationInfoDTO;

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

    public String getServiceTypeInfo() {
        return serviceTypeInfo;
    }

    public void setServiceTypeInfo(String serviceTypeInfo) {
        this.serviceTypeInfo = serviceTypeInfo;
    }

    public String getVerifyTypeInfo() {
        return verifyTypeInfo;
    }

    public void setVerifyTypeInfo(String verifyTypeInfo) {
        this.verifyTypeInfo = verifyTypeInfo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getBusinessInfo() {
        return businessInfo;
    }

    public void setBusinessInfo(String businessInfo) {
        this.businessInfo = businessInfo;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    public AuthorizationInfoDTO getAuthorizationInfoDTO() {
        return authorizationInfoDTO;
    }

    public void setAuthorizationInfoDTO(AuthorizationInfoDTO authorizationInfoDTO) {
        this.authorizationInfoDTO = authorizationInfoDTO;
    }

    @Override
    public String toString() {
        return "AuthorizerInfoDTO{" +
                "nickName='" + nickName + '\'' +
                ", headImg='" + headImg + '\'' +
                ", serviceTypeInfo='" + serviceTypeInfo + '\'' +
                ", verifyTypeInfo='" + verifyTypeInfo + '\'' +
                ", username='" + username + '\'' +
                ", principalName='" + principalName + '\'' +
                ", businessInfo='" + businessInfo + '\'' +
                ", alias='" + alias + '\'' +
                ", qrcodeUrl='" + qrcodeUrl + '\'' +
                ", authorizationInfoDTO=" + authorizationInfoDTO +
                '}';
    }
}
