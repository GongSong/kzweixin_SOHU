package com.kuaizhan.kzweixin.entity.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 通过code换取accessToken
 * Created by zixiong on 2017/7/14.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("openid")
    private String openId;

    private String scope;

    private int errcode; // 默认0
    private String errmsg;
}
