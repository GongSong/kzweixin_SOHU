package com.kuaizhan.kzweixin.entity.http.response;

import lombok.Data;

/**
 * Created by zixiong on 2017/08/04.
 */
@Data
public class CustomMsgResponse {

    private int errcode; // 默认0
    private String errmsg;
}
