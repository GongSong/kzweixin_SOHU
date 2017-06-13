package com.kuaizhan.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by fangtianyu on 6/12/17.
 */
@Data
public class UpdateOpenShareParam {
    @NotNull(message = "自定义分享变量不能为空")
    private Integer openShare;
}
