package com.kuaizhan.kzweixin.controller.vo;

import com.kuaizhan.kzweixin.enums.MsgSendType;
import com.kuaizhan.kzweixin.enums.MsgType;
import lombok.Data;

import java.util.Map;

/**
 * 消息展示对象
 * Created by liangjiateng on 2017/3/18.
 */
@Data
public class MsgVO {
    private MsgType msgType;
    private MsgSendType sendType;
    private String nickname;
    private String headImgUrl;
    private String openId;
    private Map content;
    private Long createTime;
}
