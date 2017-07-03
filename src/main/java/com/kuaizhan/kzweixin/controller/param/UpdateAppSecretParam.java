package com.kuaizhan.kzweixin.controller.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by fangtianyu on 6/7/17.
 */
@Data
public class UpdateAppSecretParam {
    @NotNull(message = "App Secret cannot be null")
    private String appSecret;
}


