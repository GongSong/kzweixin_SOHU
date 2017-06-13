package com.kuaizhan.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by fangtianyu on 6/12/17.
 */
@Data
public class UpdateAuthLoginParam {
    @NotNull(message = "授权登录变量不能为空")
    private Integer openLogin;
}
