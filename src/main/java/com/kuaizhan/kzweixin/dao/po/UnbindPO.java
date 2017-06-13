package com.kuaizhan.kzweixin.dao.po;

import lombok.Data;

/**
 * 解绑信息
 * Created by Mr.Jadyn on 2017/1/25.
 */
@Data
public class UnbindPO {

    private Long weixinAppId;
    private Integer unbindType;
    private String unbindText;
    private Integer status;
    private Long createTime;
    private Long updateTime;
}
