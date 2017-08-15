package com.kuaizhan.kzweixin.mq.consumer;

import com.kuaizhan.kzweixin.dao.po.auto.TplMsgPO;
import com.kuaizhan.kzweixin.exception.weixin.WxRequireRemoveBlackListException;
import com.kuaizhan.kzweixin.exception.weixin.WxRequireSubscribeException;
import com.kuaizhan.kzweixin.mq.dto.SendTplMsgForMassDTO;
import com.kuaizhan.kzweixin.service.TplService;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * 消费群发产生的模板消息
 * Created by zixiong on 2017/7/13.
 */
public class SendTplMsgForMassConsumer extends BaseConsumer {

    @Resource
    private TplService tplService;

    private static final Logger logger = LoggerFactory.getLogger(SendTplMsgForMassConsumer.class);

    @Override
    void onMessage(String message) {
        SendTplMsgForMassDTO dto = JsonUtil.string2Bean(message, SendTplMsgForMassDTO.class);

        long msgId;
        try {
            msgId = tplService.sendTplMsg(dto.getWeixinAppid(), dto.getTplId(), dto.getOpenId(), dto.getMsgUrl(), dto.getDataMap());
            tplService.updateTplMassIdToCache(dto.getAppId(), msgId, dto.getTplMassId());
        } catch (WxRequireRemoveBlackListException e) {
            logger.debug("[mq] openid in blacklist, abandon. appId: {} openId: {}", dto.getAppId(), dto.getOpenId());
            return;
        } catch (WxRequireSubscribeException e) {
            logger.debug("[mq] fan not subscribe, abandon. appId: {} openId: {}", dto.getAppId(), dto.getOpenId());
            return;
        }

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
