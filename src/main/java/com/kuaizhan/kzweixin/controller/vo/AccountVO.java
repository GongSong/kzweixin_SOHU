package com.kuaizhan.kzweixin.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 账户信息展示对象
 * Created by liangjiateng on 2017/3/16.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountVO {

    private Long weixinAppid;
    private String appId;
    private String appSecret;
    private String headImg;
    private String qrcode;
    private String name;
    private Integer serviceType;
    private Integer verifyType;

    private Boolean isAuthorized;
    private Long newUserCount;
    private Long newMsgCount;
    private Long userCount;
}
