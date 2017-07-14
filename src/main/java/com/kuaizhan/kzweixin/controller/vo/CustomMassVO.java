package com.kuaizhan.kzweixin.controller.vo;

import lombok.Data;

/**
 * Created by Yang on 2017/7/14.
 */
@Data
public class CustomMassVO {
    private Long customMassId;

    private Long weixinAppid;

    private Integer tagId;

    private Integer msgType;

    private Integer totalCount;

    private Integer successCount;

    private Integer rejectFailCount;

    private Integer otherFailCount;

    private Integer isTiming;

    private String publishTime;

    private Integer status;

    private String createTime;

    private String updateTime;

    private String msgJson;
}
