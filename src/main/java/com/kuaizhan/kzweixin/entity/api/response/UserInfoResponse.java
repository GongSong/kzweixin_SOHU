package com.kuaizhan.kzweixin.entity.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kuaizhan.kzweixin.cache.model.AuthLoginInfo;
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

    public AuthLoginInfo toAuthLogoinInfo() {
        AuthLoginInfo authLoginInfo = new AuthLoginInfo();
        authLoginInfo.setOpenid(openid);
        authLoginInfo.setUnionid(unionid);
        authLoginInfo.setNickname(nickname);
        authLoginInfo.setSex(sex);
        authLoginInfo.setProvince(province);
        authLoginInfo.setCity(city);
        authLoginInfo.setCountry(country);
        authLoginInfo.setHeadimgurl(headimgurl);
        return authLoginInfo;
    }
}
