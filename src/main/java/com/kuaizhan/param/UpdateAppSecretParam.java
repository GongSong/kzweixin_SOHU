package com.kuaizhan.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by fangtianyu on 6/7/17.
 */
@Data
public class UpdateAppSecretParam {
    @NotNull(message = "App Secret不能为空")
    private String appSecret;
}


