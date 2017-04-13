package com.kuaizhan.pojo.VO;

import java.util.List;

/**
 * 账户信息展示对象
 * Created by liangjiateng on 2017/3/16.
 */
public class AccountVO {

    private Long weixinAppid;
    private String appSecret;
    private String headImg;
    private List<String> interest;
    private String qrcode;
    private String name;
    private Integer serviceType;
    private Integer verifyType;

    public Long getWeixinAppid() {
        return weixinAppid;
    }

    public void setWeixinAppid(Long weixinAppid) {
        this.weixinAppid = weixinAppid;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public List<String> getInterest() {
        return interest;
    }

    public void setInterest(List<String> interest) {
        this.interest = interest;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(Integer verifyType) {
        this.verifyType = verifyType;
    }

    @Override
    public String toString() {
        return "AccountVO{" +
                "weixinAppid=" + weixinAppid +
                ", appSecret='" + appSecret + '\'' +
                ", headImg='" + headImg + '\'' +
                ", interest=" + interest +
                ", qrcode='" + qrcode + '\'' +
                ", name='" + name + '\'' +
                ", serviceType=" + serviceType +
                ", verifyType=" + verifyType +
                '}';
    }
}
