package com.kuaizhan.pojo.DO;

import lombok.Data;

/**
 * 粉丝model
 * Created by Mr.Jadyn on 2017/1/4.
 */
@Data
public class FanDO {
    private Long fanId;
    private String appId;
    private String openId;
    private String nickName;
    private Integer sex;
    private String city;
    private String province;
    private String country;
    private String language;
    private String headImgUrl;
    private Long subscribeTime;
    private String unionId;
    private String remark;
    private Long groupId;
    private String tagIdsJson;

    private Integer inBlackList;
    private Long lastInteractTime;
    private Integer status;
    private Long createTime;
    private Long updateTime;
}
