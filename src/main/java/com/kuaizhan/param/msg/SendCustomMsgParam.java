package com.kuaizhan.param.msg;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by zixiong on 2017/5/19.
 */
@Data
public class SendCustomMsgParam {
    @NotNull(message = "weixinAppid can not be null")
    private long weixinAppid;
    @NotNull(message = "openId can not be null")
    private String openId;
    @NotNull(message = "msgType can not be null")
    private Short msgType;
    @NotNull(message = "content can not be null")
    private Map content;
}
