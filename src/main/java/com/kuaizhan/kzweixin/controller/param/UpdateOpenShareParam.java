package com.kuaizhan.kzweixin.controller.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by fangtianyu on 6/12/17.
 */
@Data
public class UpdateOpenShareParam {
    @NotNull(message = "openShare cannot be null")
    private Integer openShare;
}
