package com.kuaizhan.pojo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 授权信息业务对象
 * Created by liangjiateng on 2017/3/15.
 */
public class AuthorizationInfoDTO {
    @JsonProperty("authorizer_appid")
    @JsonIgnore
    private String appId;
    @JsonProperty("authorizer_access_token")
    @JsonIgnore
    private String accessToken;
    @JsonProperty("expires_in")
    @JsonIgnore
    private Integer expiresIn;
    @JsonProperty("authorizer_refresh_token")
    @JsonIgnore
    private String refreshToken;
    @JsonProperty("func_info")
    @JsonIgnore
    private String funcInfo;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getFuncInfo() {
        return funcInfo;
    }

    public void setFuncInfo(String funcInfo) {
        this.funcInfo = funcInfo;
    }

    @Override
    public String toString() {
        return "AuthorizationInfoDTO{" +
                "appId='" + appId + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", refreshToken='" + refreshToken + '\'' +
                ", funcInfo='" + funcInfo + '\'' +
                '}';
    }
}
