package com.kuaizhan.pojo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

/**
 * 授权信息业务对象
 * Created by liangjiateng on 2017/3/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizationInfoDTO {
    @JsonProperty("authorizer_appid")
    private String appId;
    @JsonProperty("authorizer_access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    @JsonProperty("authorizer_refresh_token")
    private String refreshToken;

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
