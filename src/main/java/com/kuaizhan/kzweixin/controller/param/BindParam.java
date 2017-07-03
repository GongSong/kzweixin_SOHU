package com.kuaizhan.kzweixin.controller.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by zixiong on 2017/6/21.
 */
@Data
public class BindParam {
    @NotNull(message = "userId can not be null")
    private Long userId;
    @NotNull(message = "redirectUrl can not be null")
    private String redirectUrl;

    private Long siteId;
}
