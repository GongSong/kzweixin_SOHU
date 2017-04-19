package com.kuaizhan.pojo.DO;

import lombok.Data;

/**
 * 解绑信息
 * Created by Mr.Jadyn on 2017/1/25.
 */
@Data
public class UnbindDO {

    private Long weixinAppId;
    private Integer unbindType;
    private String unbindText;
    private Integer status;
    private Long createTime;
    private Long updateTime;
}
