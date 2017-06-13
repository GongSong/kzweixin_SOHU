package com.kuaizhan.controller.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by zixiong on 2017/5/17.
 */
@Data
public class WeixinAppidParam {
    @NotNull(message = "weixinAppid不能为空")
    private Long weixinAppid;
}
