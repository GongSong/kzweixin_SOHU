package com.kuaizhan.kzweixin.entity.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by zixiong on 2017/7/14.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoResponse extends WxBaseResponse {
    private String openid;
    private String unionid;
    private String nickname;
    private Integer sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
}
