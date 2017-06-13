package com.kuaizhan.controller.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by zixiong on 2017/4/19.
 */
@Data
public class WxSyncsPostParam {
    @NotNull(message = "uid不能为空")
    private Long uid;

    @NotNull(message = "weixinAppid不能为空")
    private Long weixinAppid;
}
