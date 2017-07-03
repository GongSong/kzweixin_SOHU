package com.kuaizhan.kzweixin.entity.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by zixiong on 2017/6/15.
 */
@Data
public class AccessTokenDTO {
    @JsonProperty("authorizer_access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    @JsonProperty("authorizer_refresh_token")
    private String refreshToken;
}
