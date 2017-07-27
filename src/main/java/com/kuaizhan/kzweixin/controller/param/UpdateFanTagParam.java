package com.kuaizhan.kzweixin.controller.param;

import lombok.Data;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by fangtianyu on 6/20/17.
 */
@Data
public class UpdateFanTagParam {
    @NotNull
    private Long weixinAppid;

    @NotNull
    @Size(min = 1, max = 20)
    private List<String> openIds;

    @Size(max = 3)
    private List<Integer> newTagIds;

    @Size(max = 3)
    private List<Integer> deleteTagIds;
}