package com.kuaizhan.controller.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by zixiong on 2017/6/7.
 */
@Data
public class AddAccountParam {
    private Long siteId;

    @NotNull(message = "userId can not be null")
    private Long userId;

    @NotNull(message = "authCode can not be null")
    private String authCode;
}
