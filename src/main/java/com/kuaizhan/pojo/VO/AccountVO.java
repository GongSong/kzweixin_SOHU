package com.kuaizhan.pojo.VO;

/**
 * 账户信息展示对象
 * Created by liangjiateng on 2017/3/16.
 */
public class AccountVO {
    private Long appId;
    private String appSecret;
    private String headImg;
    private String interest;
    private String qrcode;
    private String name;
    private Integer type;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
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

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AccountVO{" +
                "appId=" + appId +
                ", appSecret='" + appSecret + '\'' +
                ", headImg='" + headImg + '\'' +
                ", interest='" + interest + '\'' +
                ", qrcode='" + qrcode + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
