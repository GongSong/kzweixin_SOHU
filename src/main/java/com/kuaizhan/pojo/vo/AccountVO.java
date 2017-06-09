package com.kuaizhan.pojo.vo;

import lombok.Data;

/**
 * 账户信息展示对象
 * Created by liangjiateng on 2017/3/16.
 */
@Data
public class AccountVO {

    private Long weixinAppid;
    private String appId;
    private String appSecret;
    private String headImg;
    private String qrcode;
    private String name;
    private Integer serviceType;
    private Integer verifyType;
}
