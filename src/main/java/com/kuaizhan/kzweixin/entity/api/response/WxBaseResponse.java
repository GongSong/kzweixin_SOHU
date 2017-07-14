package com.kuaizhan.kzweixin.entity.api.response;

import lombok.Data;

/**
 * 微信response基类
 * Created by zixiong on 2017/7/14.
 */
@Data
public class WxBaseResponse {
    private int errcode;
    private String errmsg;
}
