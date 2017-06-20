package com.kuaizhan.kzweixin.controller.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by fangtianyu on 6/15/17.
 */
@Data
public class NewTagParam {
    @NotNull
    private Long weixinAppid;
    @NotNull
    private String tagName;
}
