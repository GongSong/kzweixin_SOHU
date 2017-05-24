package com.kuaizhan.mq;

import com.kuaizhan.constant.ErrorCode;
import com.kuaizhan.dao.mapper.TplDao;
import com.kuaizhan.exception.BusinessException;
import com.kuaizhan.exception.weixin.WxInvalidOpenIdException;
import com.kuaizhan.exception.weixin.WxInvalidTemplateException;
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
                logger.info("[deleteTpl] weixinAppid:{} tplIdShort:{}", weixinAppid, tplIdShort);
            }
            throw new BusinessException(ErrorCode.HAS_NOT_ADD_TEMPLATE_ERROR);
        } catch (WxInvalidOpenIdException e) {
            // 非法的openID, 通过微信异常的方式，做消极校验
            throw new BusinessException(ErrorCode.INVALID_OPEN_ID_ERROR);
        }

    }
}
