package com.kuaizhan.controller.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by zixiong on 2017/5/17.
 */
@Data
public class UpdateQuickRepliesParam {
    @NotNull(message = "weixinAppid不能为空")
    private Long weixinAppid;
    @NotNull(message = "quickReplies不能为空")
    private List<String> quickReplies;
}
