package com.kuaizhan.mq;

import com.kuaizhan.constant.ErrorCode;
import com.kuaizhan.dao.mapper.TplDao;
import com.kuaizhan.exception.BusinessException;
import com.kuaizhan.exception.weixin.WxInvalidOpenIdException;
import com.kuaizhan.exception.weixin.WxInvalidTemplateException;
import com.kuaizhan.exception.weixin.WxRequireSubscribeException;
import com.kuaizhan.service.TplService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by zixiong on 2017/5/24.
 */
public class SendTplMsgConsumer extends BaseMqConsumer {

    @Resource
    TplService tplService;
    @Resource
    TplDao tplDao;

    private static final Logger logger = LoggerFactory.getLogger(SendTplMsgConsumer.class);

    @Override
    protected void onMessage(Map msgMap) throws Exception {

        long weixinAppid = (long) msgMap.get("weixinAppid");
        String tplId = (String) msgMap.get("tplId");
        String tplIdShort = (String) msgMap.get("tplIdShort");
        String openId = (String) msgMap.get("openId");
        String url = (String) msgMap.get("url");
        Map dataMap = (Map) msgMap.get("dataMap");

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
