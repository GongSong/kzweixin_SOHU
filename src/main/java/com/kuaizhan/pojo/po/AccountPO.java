package com.kuaizhan.pojo.po;

import lombok.Data;

/**
 * 账户数据对象
 * Created by liangjiateng on 2017/3/15.
 */
@Data
public class AccountPO {

    private Long weixinAppId;
    private Long siteId;
    private String accessToken;
    private String refreshToken;
    private Long expiresTime;
    private String appId;
    private String appSecret;
    private String funcInfoJson;
    private String nickName;
    private String headImg;
    private Integer serviceType;


    private Integer verifyType;
    private String userName;
    private String alias;
    private String businessInfoJson;
    private String qrcodeUrl;
    private String qrcodeUrlKz;
    private String menuJson;
    private String interestJson;
    private String advancedFuncInfoJson;
    private String previewOpenId;
    private Long bindTime;
    private Long unbindTime;
    private Integer isDel;
    private Long createTime;
    private Long updateTime;
}
