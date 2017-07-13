package com.kuaizhan.kzweixin.mq.consumer;

import com.kuaizhan.kzweixin.dao.po.auto.TplMsgPO;
import com.kuaizhan.kzweixin.mq.dto.SendTplMsgForMassDTO;
import com.kuaizhan.kzweixin.service.TplService;
import com.kuaizhan.kzweixin.utils.JsonUtil;

import javax.annotation.Resource;

/**
 * 消费群发产生的模板消息
 * Created by zixiong on 2017/7/13.
 */
public class SendTplMsgForMassConsumer extends BaseConsumer {

    @Resource
    private TplService tplService;

    @Override
    void onMessage(String message) {
        SendTplMsgForMassDTO dto = JsonUtil.string2Bean(message, SendTplMsgForMassDTO.class);

        long msgId = tplService.sendTplMsg(dto.getWeixinAppid(), dto.getTplId(), dto.getOpenId(), dto.getMsgUrl(), dto.getDataMap());

        TplMsgPO tplMsgPO = new TplMsgPO();
        tplMsgPO.setMsgId(msgId);
        tplMsgPO.setTplMassId(dto.getTplMassId());
        tplMsgPO.setSender(dto.getAppId());
        tplMsgPO.setReceiver(dto.getOpenId());
        tplMsgPO.setTplId(dto.getTplId());
        tplMsgPO.setMsgUrl(dto.getMsgUrl());
        tplMsgPO.setMsgContent(JsonUtil.bean2String(dto.getDataMap()));
        tplService.addTplMsg(tplMsgPO);
    }
}
