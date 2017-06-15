package com.kuaizhan.kzweixin.entity.account;

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
    @JsonProperty("service_type_info")
    private IdObject serviceTypeInfo;
    @JsonProperty("verify_type_info")
    private IdObject verifyTypeInfo;
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

    @Data
    public class IdObject {
        private Integer id;
    }
}
