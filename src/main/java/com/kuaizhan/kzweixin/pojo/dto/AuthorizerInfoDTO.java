package com.kuaizhan.kzweixin.pojo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 公众号帐号基本信息业务对象
 * Created by liangjiateng on 2017/3/15.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizerInfoDTO {
    @JsonProperty("nick_name")
    private String nickName;
    @JsonProperty("head_img")
    private String headImg;

    private String serviceTypeInfo;

    private String verifyTypeInfo;

    @JsonProperty("user_name")
    private String username;
    @JsonProperty("principal_name")
    private String principalName;

    private String businessInfo;

    @JsonProperty("alias")
    private String alias;
    @JsonProperty("qrcode_url")
    private String qrcodeUrl;
}
