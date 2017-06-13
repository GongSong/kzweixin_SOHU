package com.kuaizhan.kzweixin.controller.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by zixiong on 2017/5/15.
 */
@Data
public class AddSysTplParam {
    @NotNull(message = "siteId不能为空")
    private Long siteId;
    @NotNull(message = "templateId不能为空")
    private String templateId;
}
