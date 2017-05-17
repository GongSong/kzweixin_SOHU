package com.kuaizhan.service.impl;

import com.kuaizhan.config.WxApiConfig;
import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.exception.deprecated.business.SendCustomMsgException;
import com.kuaizhan.exception.deprecated.system.EncryptException;
import com.kuaizhan.exception.common.XMLParseException;
import com.kuaizhan.pojo.po.AccountPO;
import com.kuaizhan.pojo.po.MsgPO;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.MsgService;
import com.kuaizhan.service.WeixinFanService;
import com.kuaizhan.service.WeixinMsgService;
import com.kuaizhan.utils.HttpClientUtil;
import com.kuaizhan.utils.weixin.WXBizMsgCrypt;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by liangjiateng on 2017/3/20.
 */
@Service("weixinMsgService")
public class WeixinMsgServiceImpl implements WeixinMsgService {

    @Resource
    WeixinFanService weixinFanService;
    @Resource
    AccountService accountService;
    @Resource
    MsgService msgService;

    private static final Logger logger = Logger.getLogger(WeixinMsgServiceImpl.class);

    @Override
    public String handleWeixinPushMsg(String appId, String signature, String timestamp, String nonce, String postData) throws EncryptException, XMLParseException {
        AccountPO accountPO = accountService.getAccountByAppId(appId);
        if (accountPO == null) {
            logger.error("[WeixinPush] get unknown appId");
        }
        //解密消息
        String msg;
        try {
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(ApplicationConfig.WEIXIN_TOKEN, ApplicationConfig.WEIXIN_AES_KEY, ApplicationConfig.WEIXIN_APPID_THIRD);
            msg = wxBizMsgCrypt.decryptMsg(signature, timestamp, nonce, postData);
        } catch (Exception e) {
            throw new EncryptException(e);
        }
        //解析消息
        Document document;
        try {
            document = DocumentHelper.parseText(msg);
        } catch (Exception e) {
            throw new XMLParseException("[Weixin:handleWeiXinPushMsg] xml parse failed", e);
        }
        Element root = document.getRootElement();
        Element msgType = root.element("MsgType");
        Element event = root.element("Event");
        if (event != null) {
            switch (event.getText()) {
                //关注
                case "subscribe":
                    weixinFanService.subscribe(accountPO, msg);
                    break;
                //取关
                case "unsubscribe":
                    weixinFanService.unSubscribe(accountPO, msg);
                    break;
                //群发
                case "MASSSENDJOBFINISH":
                    break;
            }

        } else if (msgType != null) {
            Element openId = root.element("FromUserName");
            JSONObject jsonObject = new JSONObject();
            MsgPO msgPO = new MsgPO();

            switch (msgType.getText()) {
                case "text":
                    Element content = root.element("Content");

                    //TODO: emoji表情不能存入数据库的问题

                    jsonObject.put("content", content.getText().replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "【表情】"));
                    msgPO.setAppId(appId);
                    msgPO.setContent(jsonObject.toString());
                    msgPO.setOpenId(openId.getText());
//                    msgPO.setStatus(1);
                    msgPO.setType(1);
                    msgPO.setSendType(1);
                    msgService.insertMsg(accountPO.getSiteId(), accountPO.getAppId(), msgPO);
                    break;
                case "image":
                    Element mediaId = root.element("MediaId");
                    Element picUrl = root.element("PicUrl");
                    //做一层json封装
                    jsonObject.put("media_id", mediaId.getText());
                    jsonObject.put("pic_url", picUrl.getText());
                    //存到mysql
                    msgPO.setAppId(appId);
                    msgPO.setContent(jsonObject.toString());
                    msgPO.setOpenId(openId.getText());
                    msgPO.setType(2);
//                    msgPO.setStatus(1);
                    msgPO.setSendType(1);
                    msgService.insertMsg(accountPO.getSiteId(), accountPO.getAppId(), msgPO);
                    break;
                case "location":
                    Element x = root.element("Location_X");
                    Element y = root.element("Location_Y");
                    Element scale = root.element("Scale");
                    Element label = root.element("Label");
                    //做一层json封装
                    jsonObject.put("location_x", x.getText());
                    jsonObject.put("location_y", y.getText());
                    jsonObject.put("scale", scale.getText());
                    jsonObject.put("label", label.getText());
                    //存到mysql
                    msgPO.setAppId(appId);
                    msgPO.setContent(jsonObject.toString());
                    msgPO.setOpenId(openId.getText());
                    msgPO.setType(6);
//                    msgPO.setStatus(1);
                    msgPO.setSendType(1);
                    msgService.insertMsg(accountPO.getSiteId(), accountPO.getAppId(), msgPO);
                    break;
                case "link":
                    Element title = root.element("Title");
                    Element description = root.element("Description");
                    Element url = root.element("Url");
                    //做一层json封装
                    jsonObject.put("title", title.getText());
                    jsonObject.put("description", description.getText());
                    jsonObject.put("url", url.getText());
                    //存到mysql
                    msgPO.setAppId(appId);
                    msgPO.setContent(jsonObject.toString());
                    msgPO.setOpenId(openId.getText());
                    msgPO.setType(7);
//                    msgPO.setStatus(1);
                    msgPO.setSendType(1);
                    msgService.insertMsg(accountPO.getSiteId(), accountPO.getAppId(), msgPO);
                    break;
                default:
                    //做一层json封装
                    jsonObject.put("content", "【收到不支持的消息类型，暂无法显示】");
                    msgPO.setAppId(appId);
                    msgPO.setContent(jsonObject.toString());
                    msgPO.setOpenId(openId.getText());
//                    msgPO.setStatus(1);
                    msgPO.setType(1);
                    msgPO.setSendType(1);
                    msgService.insertMsg(accountPO.getSiteId(), accountPO.getAppId(), msgPO);
                    break;

            }
        }
        return "success";
    }

    @Override
    public int sendCustomMsg(String appId, String accessToken, String openId, int msgType, JSONObject content) throws SendCustomMsgException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser", openId);
        switch (msgType) {
            case 1:
                jsonObject.put("msgtype", "text");
                jsonObject.put("text", content);
                break;
            case 2:
                jsonObject.put("msgtype", "image");
                jsonObject.put("image", content);
                break;
            case 10:
                jsonObject.put("msgtype", "news");
                jsonObject.put("news", content);
                break;
        }
        String result = HttpClientUtil.postJson(WxApiConfig.sendByOpenIdUrl(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);
        if (returnJson.getInt("errcode") != 0) {
            throw new SendCustomMsgException();
        }
        return 1;
    }


}
