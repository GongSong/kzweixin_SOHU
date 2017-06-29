package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.dao.po.auto.AccountPO;
import com.kuaizhan.kzweixin.dao.po.auto.ActionPO;
import com.kuaizhan.kzweixin.entity.WxData;
import com.kuaizhan.kzweixin.entity.action.NewsResponse;
import com.kuaizhan.kzweixin.entity.action.TextResponse;
import com.kuaizhan.kzweixin.enums.ActionType;
import com.kuaizhan.kzweixin.enums.ResponseType;
import com.kuaizhan.kzweixin.exception.common.XMLParseException;
import com.kuaizhan.kzweixin.manager.KzManager;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.ActionService;
import com.kuaizhan.kzweixin.service.CommonService;
import com.kuaizhan.kzweixin.service.WxPushService;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
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
    private WxThirdPartServiceImpl wxThirdPartService;

    private static final Logger logger = LoggerFactory.getLogger(WxPushServiceImpl.class);

    @Override
    public String handleEventPush(String appId, String signature, String timestamp, String nonce, String xmlStr) {

        logger.info("xmlStr:" + xmlStr);
        kzStat("a000", appId);

        //解析消息
        Document document;
        try {
           document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            throw new XMLParseException("[handleEventPush] xml parse failed, xmlStr:" + xmlStr, e);
        }
        Element root = document.getRootElement();
        WxData wxData = getWxData(root, appId);

        String result = null;
        String msgType = wxData.getMsgType();

        // ************ Event 事件 *****************
        if ("event".equals(msgType)) {
            result = handleEventMsg(wxData);

        // ************ Text 事件 *****************
        } else if ("text".equals(msgType)) {
            result = handleTextMsg(wxData);
        }

        // java代码成功处理了则返回，否则继续调用php
        if (result != null) {
            return result;
        }
        // 否则调用php处理请求
        String phpResult = KzManager.kzResponseMsg(appId, timestamp, nonce, xmlStr);
        logger.info("phpResult:", phpResult);

        return phpResult;
    }

    private String handleTextMsg(WxData wxData) {

        kzStat("a200", wxData.getAppId());
        AccountPO accountPO = accountService.getAccountByAppId(wxData.getAppId());

        return handleActions(accountPO.getWeixinAppid(), wxData, ActionType.REPLY);
    }

    /**
     * 处理msgType == "event"
     */
    private String handleEventMsg(WxData wxData) {

        kzStat("a100", wxData.getAppId());
        AccountPO accountPO = accountService.getAccountByAppId(wxData.getAppId());

        if ("subscribe".equals(wxData.getEvent())) {

            kzStat("a110", wxData.getAppId());
            if (wxData.getEventKey() != null) {
                // 带场景的参数二维码等操作
            } else {
                // 处理Action
                return handleActions(accountPO.getWeixinAppid(), wxData, ActionType.SUBSCRIBE);
            }
        }

        return null;
    }

    /**
     * 处理动作
     */
    public String handleActions(long weixinAppid, WxData wxData, ActionType actionType) {

        List<ActionPO> actionPOS = actionService.getActions(weixinAppid, actionType);

        //TODO: 先按业务排序
        for (ActionPO actionPO: actionPOS) {

            // 判断是否触发action
            if (! actionService.shouldAction(actionPO, wxData.getContent())) {
                continue;
            }

            int responseType = actionPO.getResponseType();
            String fromUserName = wxData.getFromUserName();
            String toUserName = wxData.getToUserName();

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
    private WxData getWxData(Element root, String appId) {
        WxData wxData = new WxData();
        // 必有字段
        wxData.setAppId(appId);
        wxData.setFromUserName(root.elementText("FromUserName"));
        wxData.setToUserName(root.elementText("ToUserName"));
        wxData.setMsgType(root.elementText("MsgType"));
        wxData.setCreateTime(root.elementText("CreateTime"));
        // 可能为空字段
        wxData.setEvent(root.elementText("Event"));
        wxData.setEventKey(root.elementText("EventKey"));
        wxData.setContent(root.elementText("Content"));
        return wxData;
    }

    /**
     * 统计事件
     */
    private void kzStat(String traceKey, String appId) {
        commonService.kzStat("weixin", traceKey);
        commonService.kzStat("weixin", traceKey + appId);
    }
}
