package com.kuaizhan.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by fangtianyu on 6/12/17.
 */
@Data
public class UpdateAuthLoginParam {
    @NotNull(message = "openLogin cannot be null")
    private Integer openLogin;
}
