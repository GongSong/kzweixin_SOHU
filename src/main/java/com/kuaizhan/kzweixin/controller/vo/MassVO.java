package com.kuaizhan.kzweixin.controller.vo;

import lombok.Data;

/**
 * Created by Yang on 2017/7/14.
 */
@Data
public class MassVO {
    private Long massId;

    private Long weixinAppid;

    private Integer responseType;

    private String msgId;

    private String statusMsg;

    private Integer totalCount;

    private Integer filterCount;

    private Integer sentCount;

    private Integer errorCount;

    private Integer groupId;

    private Integer isTiming;

    private String publishTime;

    private Integer status;

    private String createTime;

    private String updateTime;

    private String responseJson;
}
