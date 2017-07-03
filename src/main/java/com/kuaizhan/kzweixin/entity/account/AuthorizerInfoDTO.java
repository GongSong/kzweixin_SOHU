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

    @JsonProperty("authorizer_info")
    private Info info;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Info {
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
        private BusinessInfo businessInfo;
        @JsonProperty("alias")
        private String alias;
        @JsonProperty("qrcode_url")
        private String qrcodeUrl;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IdObject {
        @JsonProperty("id")
        private Integer id;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BusinessInfo {
        @JsonProperty("open_store")
        private Integer openStore;
        @JsonProperty("open_scan")
        private Integer openScan;
        @JsonProperty("open_pay")
        private Integer openPay;
        @JsonProperty("open_card")
        private Integer openCard;
        @JsonProperty("open_shake")
        private Integer openShake;
    }
}
