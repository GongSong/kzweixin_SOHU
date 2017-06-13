package com.kuaizhan.kzweixin.mq.consumer;

import com.kuaizhan.kzweixin.dao.mapper.TplDao;
import com.kuaizhan.kzweixin.exception.weixin.WxInvalidTemplateException;
import com.kuaizhan.kzweixin.exception.weixin.WxRequireSubscribeException;
import com.kuaizhan.kzweixin.mq.dto.SendTplMsgDTO;
import com.kuaizhan.kzweixin.service.TplService;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by zixiong on 2017/5/24.
 */
public class SendTplMsgConsumer extends BaseConsumer {

    @Resource
    private TplService tplService;
    @Resource
    private TplDao tplDao;

    private static final Logger logger = LoggerFactory.getLogger(SendTplMsgConsumer.class);

    @Override
    public void onMessage(String message) {

        SendTplMsgDTO dto = JsonUtil.string2Bean(message, SendTplMsgDTO.class);

        long weixinAppid = dto.getWeixinAppid();
        String tplId = dto.getTplId();
        String tplIdShort = dto.getTplIdShort();
        String openId = dto.getOpenId();
        String url = dto.getUrl();
        Map dataMap = dto.getDataMap();

        try {
            tplService.sendTplMsg(weixinAppid, tplId, openId, url, dataMap);
        } catch (WxInvalidTemplateException e) {
            // 模板id已经不可用，删除之
            boolean deleted = tplDao.deleteTpl(weixinAppid, tplIdShort);
            if (deleted) {
                logger.warn("[deleteTpl][报错量太多时需要处理] weixinAppid:{} tplIdShort:{}", weixinAppid, tplIdShort);
            }
        } catch (WxRequireSubscribeException e) {
            // 社区可能给未关注的人发消息，先忽略错误
            logger.info("[mq:sendTplMsg] send msg to not subscribed openI, e.getMessage: {}", e.getMessage());
        }
    }
}
