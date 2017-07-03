package com.kuaizhan.kzweixin.entity.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 授权信息业务对象
 * Created by liangjiateng on 2017/3/15.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizationInfoDTO {

    @JsonProperty("authorization_info")
    private Info info;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Info {
        @JsonProperty("authorizer_appid")
        private String appId;
        @JsonProperty("authorizer_access_token")
        private String accessToken;
        @JsonProperty("expires_in")
        private Integer expiresIn;
        @JsonProperty("authorizer_refresh_token")
        private String refreshToken;
        @JsonProperty("func_info")
        private List<FuncInfo> funcInfoList;
    }
}
