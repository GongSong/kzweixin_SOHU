package com.kuaizhan.kzweixin.controller.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by fangtianyu on 6/15/17.
 */
@Data
public class TagNameParam {
    @NotNull
    private Long weixinAppid;
    @NotNull
    @Size(max = 30)
    private String tagName;
}
