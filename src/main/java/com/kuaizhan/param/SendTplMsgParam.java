package com.kuaizhan.param;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by zixiong on 2017/5/15.
 */
@Data
public class SendTplMsgParam {
    @NotNull(message = "siteId不能为空")
    private Long siteId;
    @NotNull(message = "模板编号不能为空")
    private String templateId;
    @NotNull(message = "openId不能为空")
    private String openId;
    @NotNull(message = "数据不能为空")
    private Map data;

    private String url;
}
