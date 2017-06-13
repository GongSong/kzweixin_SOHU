package com.kuaizhan.controller.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by zixiong on 2017/5/15.
 */
@Data
public class SendTplMsgParam {
    @NotNull(message = "appId不能为空")
    private String appId;
    @NotNull(message = "模板编号不能为空")
    private String templateId;
    @NotNull(message = "openId不能为空")
    private String openId;
    @NotNull(message = "数据不能为空")
    private Map data;

    private String url;
}
