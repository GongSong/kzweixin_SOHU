package com.kuaizhan.kzweixin.controller.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by fangtianyu on 6/16/17.
 */
@Data
public class UpdateTagParam {
    @NotNull
    private Long weixinAppid;
    @NotNull
    private String newTag;
}
