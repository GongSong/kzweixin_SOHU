package com.kuaizhan.kzweixin.mq.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * Created by zixiong on 2017/7/13.
 */
@Data
public class SendTplMsgForMassDTO {
    @JsonProperty("weixin_appid")
    private Long weixinAppid;
    @JsonProperty("app_id")
    private String appId;
    @JsonProperty("open_id")
    private String openId;
    @JsonProperty("tpl_id")
    private String tplId;
    @JsonProperty("msg_url")
    private String msgUrl;
    @JsonProperty("msg_content")
    private Map dataMap;
    @JsonProperty("tpl_mass_id")
    private Long tplMassId;
}
