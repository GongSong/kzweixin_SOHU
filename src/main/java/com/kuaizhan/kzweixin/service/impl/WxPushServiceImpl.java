package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.dao.po.auto.AccountPO;
import com.kuaizhan.kzweixin.dao.po.auto.ActionPO;
import com.kuaizhan.kzweixin.entity.XmlData;
import com.kuaizhan.kzweixin.entity.action.NewsResponse;
import com.kuaizhan.kzweixin.entity.action.TextResponse;
import com.kuaizhan.kzweixin.enums.ActionType;
import com.kuaizhan.kzweixin.enums.ResponseType;
import com.kuaizhan.kzweixin.exception.account.AccountNotExistException;
import com.kuaizhan.kzweixin.manager.KzManager;
import com.kuaizhan.kzweixin.service.*;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import com.kuaizhan.kzweixin.utils.XmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zixiong on 2017/6/23.
 */
@Service
public class WxPushServiceImpl implements WxPushService {

    @Resource
    private CommonService commonService;
    @Resource
    private AccountService accountService;
    @Resource
    private ActionService actionService;
    @Resource
    private FanService fanService;
    @Resource
    private WxThirdPartService wxThirdPartService;
    @Resource
    private TplService tplService;

    private static final String SUCCESS_RESULT = "success";

    private static final Logger logger = LoggerFactory.getLogger(WxPushServiceImpl.class);

    @Override
    public String handleEventPush(String appId, String signature, String timestamp, String nonce, String xmlStr) {

        logger.debug("################## xmlStr: {}", xmlStr);

        // 校验appId是否存在，大量解除绑定，但是授权还在的用户
        AccountPO accountPO;
        try {
            // 量大了会有缓存击穿的问题
            accountPO = accountService.getAccountByAppId(appId);
        } catch (AccountNotExistException e) {
            return SUCCESS_RESULT;
        }

        kzStat("a000", appId);

        //解析消息
        Document document = XmlUtil.parseXml(xmlStr);
        XmlData xmlData = getWxDataFromXml(document, appId);

        String result = null;
        String msgType = xmlData.getMsgType();

        // ************ Event 事件 *****************
        if ("event".equals(msgType)) {
            result = handleEventMsg(xmlData, accountPO);

        // ************ Text 事件 *****************
        } else if ("text".equals(msgType)) {
            result = handleTextMsg(xmlData, accountPO);
        }

        // java代码成功处理了则返回，否则继续调用php
        if (result != null) {
            return result;
        }

        // 调用php处理请求
        logger.debug("######### appId: {}, timestamp: {}, nonce: {}, xmlStr: {}", appId, timestamp, nonce, xmlStr);
        String phpResult = KzManager.kzResponseMsg(appId, timestamp, nonce, xmlStr);
        logger.debug("############### phpResult: {}", phpResult);

        return phpResult;
    }

    @Override
    public String handleTestEventPush(String timestamp, String nonce, String xmlStr) {
        return KzManager.kzResponseTest(timestamp, nonce, xmlStr);
    }

    private String handleTextMsg(XmlData xmlData, AccountPO accountPO) {

        kzStat("a200", xmlData.getAppId());

        return handleActions(accountPO.getWeixinAppid(), xmlData, ActionType.REPLY);
    }

    /**
     * 处理msgType == "event"
     */
    private String handleEventMsg(XmlData xmlData, AccountPO accountPO) {

        kzStat("a100", xmlData.getAppId());

        Element xmlRoot = xmlData.getXmlRoot();
        String eventType = xmlRoot.elementText("Event");
        String eventKey = xmlRoot.elementText("EventKey");

        if ("subscribe".equals(eventType)) {

            kzStat("a110", xmlData.getAppId());

            // 添加粉丝信息
            fanService.asyncAddFan(xmlData.getAppId(), xmlData.getOpenId());

            if (StringUtils.isNotBlank(eventKey)) {
                // 带场景的参数二维码等操作
            } else {
                // 处理Action
                return handleActions(accountPO.getWeixinAppid(), xmlData, ActionType.SUBSCRIBE);
            }
        }
        else if ("LOCATION".equals(eventType)) {

            kzStat("a140", xmlData.getAppId());
            fanService.refreshInteractionTime(xmlData.getAppId(), xmlData.getOpenId());
            fanService.asyncUpdateFan(xmlData.getAppId(), xmlData.getOpenId());

            return SUCCESS_RESULT;

        }
        else if ("VIEW".equals(eventType)) {

            kzStat("a160", xmlData.getAppId());
            fanService.refreshInteractionTime(xmlData.getAppId(), xmlData.getOpenId());
            fanService.asyncUpdateFan(xmlData.getAppId(), xmlData.getOpenId());

            return SUCCESS_RESULT;

        }
        else if ("TEMPLATESENDJOBFINISH".equals(eventType)) {

            kzStat("a1a0", xmlData.getAppId());

            Long msgId = Long.parseLong(xmlRoot.elementText("MsgID"));
            String status = xmlRoot.elementText("Status");

            int statusCode = -1;
            if ("success".equals(status)) {
                statusCode = 2;
            } else if ("failed:user block".equals(status)) {
                statusCode = 3;
            } else if ("failed: system failed".equals(status)) {
                statusCode = 4;
            }

            tplService.updateTplStatus(msgId, statusCode);
            return SUCCESS_RESULT;

        }

            return null;
    }

    /**
     * 处理动作
     */
    private String handleActions(long weixinAppid, XmlData xmlData, ActionType actionType) {

        Element xmlRoot = xmlData.getXmlRoot();
        List<ActionPO> actionPOS = actionService.getActions(weixinAppid, actionType);

        //TODO: 先按业务排序
        for (ActionPO actionPO: actionPOS) {

            // 判断是否触发action
            String keyword = xmlRoot.elementText("Content");
            if (! actionService.shouldAction(actionPO, keyword)) {
                continue;
            }

            int responseType = actionPO.getResponseType();
            String fromUserName = xmlData.getFromUserName();
            String toUserName = xmlData.getToUserName();

            if (responseType == ResponseType.TEXT.getValue()) {

                TextResponse textResponse = JsonUtil.string2Bean(actionPO.getResponseJson(), TextResponse.class);
                return getTextResult(fromUserName, toUserName, textResponse.getContent());

            } else if (responseType == ResponseType.NEWS.getValue()) {
                NewsResponse newsResponse = JsonUtil.string2Bean(actionPO.getResponseJson(), NewsResponse.class);
                return getNewsResult(fromUserName, toUserName, newsResponse.getNews());
            }
        }
        return null;
    }

    /**
     * 文本回复xml组装
     */
    private String getTextResult(String toUserName, String fromUserName, String content) {
        String tpl =
                "<xml>" +
                    "<ToUserName><![CDATA[%s]]></ToUserName>" +
                    "<FromUserName><![CDATA[%s]]></FromUserName>" +
                    "<CreateTime><![CDATA[%s]]></CreateTime>" +
                    "<MsgType><![CDATA[text]]></MsgType>" +
                    "<Content><![CDATA[%s]]></Content>" +
                "</xml>";
        String result = String.format(tpl, toUserName, fromUserName, DateUtil.curSeconds(), content);
        return wxThirdPartService.encryptMsg(result);
    }

    /**
     * 链接组回复xml组装
     */
    private String getNewsResult(String toUserName, String fromUserName, List<NewsResponse.News> news) {

        String tpl =
                "<xml>" +
                    "<ToUserName><![CDATA[%s]]></ToUserName>" +
                    "<FromUserName><![CDATA[%s]]></FromUserName>" +
                    "<CreateTime><![CDATA[%s]]></CreateTime>" +
                    "<MsgType><![CDATA[news]]></MsgType>" +
                    "<ArticleCount><![CDATA[%s]]></ArticleCount>" +
                    "<Articles>" +
                        "%s" +
                    "</Articles>" +
                "</xml>";

        String tplItem =
                "<item>" +
                    "<Title><![CDATA[%s]]></Title>" +
                    "<Description><![CDATA[%s]]></Description>" +
                    "<PicUrl><![CDATA[%s]]></PicUrl>" +
                    "<Url><![CDATA[%s]]></Url>" +
                 "</item>";

        StringBuilder itemBuilder = new StringBuilder();
        for(NewsResponse.News article: news) {
            String itemStr = String.format(tplItem,
                    article.getTitle(),
                    article.getDescription(),
                    article.getPicUrl(),
                    article.getUrl());
            itemBuilder.append(itemStr);
        }

        String result = String.format(tpl, toUserName, fromUserName,
                DateUtil.curSeconds(), news.size(), itemBuilder.toString());
        return wxThirdPartService.encryptMsg(result);
    }

    /**
     * 从xml解析出wxData
     */
    private XmlData getWxDataFromXml(Document document, String appId) {
        Element xmlRoot = document.getRootElement();

        XmlData xmlData = new XmlData();
        // 必有字段
        xmlData.setAppId(appId);
        xmlData.setFromUserName(xmlRoot.elementText("FromUserName"));
        xmlData.setOpenId(xmlData.getFromUserName());
        xmlData.setToUserName(xmlRoot.elementText("ToUserName"));
        xmlData.setMsgType(xmlRoot.elementText("MsgType"));
        xmlData.setCreateTime(xmlRoot.elementText("CreateTime"));

        xmlData.setXmlRoot(xmlRoot);

        return xmlData;
    }

    /**
     * 统计事件
     */
    private void kzStat(String traceKey, String appId) {
        try {
            commonService.kzStat("weixin", traceKey);
            commonService.kzStat("weixin", traceKey + appId);
        } catch (Exception e) {
            // 遇到过mq挂了的情况，不能因为统计信息，影响到不需要mq的回调业务
            logger.error("[WxPush] kzStat failed", e);
        }
    }
}
