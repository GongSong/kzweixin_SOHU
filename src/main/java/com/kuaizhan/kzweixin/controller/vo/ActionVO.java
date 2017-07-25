package com.kuaizhan.kzweixin.controller.vo;

import lombok.Data;

import java.util.Map;

/**
 * Created by zixiong on 2017/07/25.
 */
@Data
public class ActionVO {
    private Integer id;
    private String bizCode;
    private String keyword;
    private Integer actionType;
    private Integer responseType;
    private Map responseJson;
    private Boolean status;
}
