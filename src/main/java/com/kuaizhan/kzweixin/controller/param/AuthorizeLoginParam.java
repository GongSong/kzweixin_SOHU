package com.kuaizhan.kzweixin.controller.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by zixiong on 2017/7/13.
 */
@Data
public class AuthorizeLoginParam {
    @NotNull
    private String appId;
    @NotNull
    private String redirectUrl;
    @NotNull
    private String scope;
}
