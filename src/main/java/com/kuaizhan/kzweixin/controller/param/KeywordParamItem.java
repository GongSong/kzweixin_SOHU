package com.kuaizhan.kzweixin.controller.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by fangtianyu on 7/31/17.
 */
@Data
public class KeywordParamItem {
    @NotNull
    @Size(min = 1, max = 30, message = "关键词不能超过30个字符")
    private String keyword;
    @NotNull
    private String type;
}
