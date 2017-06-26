package com.kuaizhan.kzweixin.controller.param;

import lombok.Data;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by fangtianyu on 6/22/17.
 */
@Data
public class UserBlacklistParam {
    @NotNull
    private Long weixinAppid;

    @NotNull
    @Size(min = 1, max = 20)
    private List<String> fansOpenId;

    @NotNull
    private Boolean setBlacklist;
}
