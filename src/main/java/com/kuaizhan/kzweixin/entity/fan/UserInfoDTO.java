package com.kuaizhan.kzweixin.entity.fan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 公众号粉丝基本信息业务对象
 * Created by fangtianyu on 6/28/17.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoDTO {

    @JsonProperty("subscribe")
    private Integer status;

    @JsonProperty("openid")
    private String openId;

    private Integer sex;

    @JsonProperty("nickname")
    private String nickName;

    private String language;

    private String city;

    private String province;

    private String country;

    @JsonProperty("headimgurl")
    private String headImgUrl;

    @JsonProperty("subscribe_time")
    private Integer subscribeTime;

    @JsonProperty("unionid")
    private String unionId;

    private String remark;

    @JsonProperty("groupid")
    private Integer groupId;

    @JsonProperty("tagid_list")
    private List<Integer> tagIdsList;

}
