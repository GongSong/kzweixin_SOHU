package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApiConfig;
import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.redis.RedisMsgDao;
import com.kuaizhan.exception.business.AccountNotExistException;
import com.kuaizhan.exception.business.SendCustomMsgException;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.EncryptException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.exception.system.XMLParseException;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DO.MsgDO;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.MsgService;
import com.kuaizhan.service.WeixinFanService;
import com.kuaizhan.service.WeixinMsgService;
import com.kuaizhan.utils.HttpClientUtil;
import com.kuaizhan.utils.weixin.WXBizMsgCrypt;
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
    @Resource
    RedisMsgDao redisMsgDao;

    @Override
    public String handleWeixinPushMsg(String appId, String signature, String timestamp, String nonce, String postData) throws EncryptException, XMLParseException, DaoException, AccountNotExistException, RedisException {
        //TODO:使用MQ进行处理
        AccountDO accountDO = accountService.getAccountByAppId(appId);
        if (accountDO == null) {
            throw new AccountNotExistException();
        }
        //解密消息
        String msg;
        try {
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(ApplicationConfig.WEIXIN_TOKEN, ApplicationConfig.WEIXIN_AES_KEY, ApplicationConfig.WEIXIN_APPID_THIRD);
            msg = wxBizMsgCrypt.decryptMsg(signature, timestamp, nonce, postData);
        } catch (Exception e) {
            throw new EncryptException(e.getMessage());
        }
        //解析消息
        Document document;
        try {
            document = DocumentHelper.parseText(msg);
        } catch (Exception e) {
            throw new XMLParseException(e.getMessage());
        }
        Element root = document.getRootElement();
        Element msgType = root.element("MsgType");
        Element event = root.element("Event");
        if (event != null) {
            switch (event.getText()) {
                //关注
                case "subscribe":
                    weixinFanService.subscribe(accountDO, msg);
                    break;
                //取关
                case "unsubscribe":
                    weixinFanService.unSubscribe(accountDO, msg);
                    break;
                //群发
                case "MASSSENDJOBFINISH":
                    break;
            }

        } else if (msgType != null) {
            Element openId = root.element("FromUserName");
            JSONObject jsonObject = new JSONObject();
            MsgDO msgDO = new MsgDO();

            switch (msgType.getText()) {
                case "text":
                    Element content = root.element("Content");

                    //TODO: emoji表情不能存入数据库的问题

                    jsonObject.put("content", content.getText().replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "【表情】"));
                    msgDO.setAppId(appId);
                    msgDO.setContent(jsonObject.toString());
                    msgDO.setOpenId(openId.getText());
                    msgDO.setStatus(1);
                    msgDO.setType(1);
                    msgDO.setSendType(1);
                    msgService.insertMsg(accountDO.getSiteId(), accountDO.getAppId(), msgDO);
                    break;
                case "image":
                    Element mediaId = root.element("MediaId");
                    Element picUrl = root.element("PicUrl");
                    //做一层json封装
                    jsonObject.put("media_id", mediaId.getText());
                    jsonObject.put("pic_url", picUrl.getText());
                    //存到mysql
                    msgDO.setAppId(appId);
                    msgDO.setContent(jsonObject.toString());
                    msgDO.setOpenId(openId.getText());
                    msgDO.setType(2);
                    msgDO.setStatus(1);
                    msgDO.setSendType(1);
                    msgService.insertMsg(accountDO.getSiteId(), accountDO.getAppId(), msgDO);
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
                    msgDO.setAppId(appId);
                    msgDO.setContent(jsonObject.toString());
                    msgDO.setOpenId(openId.getText());
                    msgDO.setType(6);
                    msgDO.setStatus(1);
                    msgDO.setSendType(1);
                    msgService.insertMsg(accountDO.getSiteId(), accountDO.getAppId(), msgDO);
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
                    msgDO.setAppId(appId);
                    msgDO.setContent(jsonObject.toString());
                    msgDO.setOpenId(openId.getText());
                    msgDO.setType(7);
                    msgDO.setStatus(1);
                    msgDO.setSendType(1);
                    msgService.insertMsg(accountDO.getSiteId(), accountDO.getAppId(), msgDO);
                    break;
                default:
                    //做一层json封装
                    jsonObject.put("content", "【收到不支持的消息类型，暂无法显示】");
                    msgDO.setAppId(appId);
                    msgDO.setContent(jsonObject.toString());
                    msgDO.setOpenId(openId.getText());
                    msgDO.setStatus(1);
                    msgDO.setType(1);
                    msgDO.setSendType(1);
                    msgService.insertMsg(accountDO.getSiteId(), accountDO.getAppId(), msgDO);
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
        String result = HttpClientUtil.postJson(ApiConfig.sendByOpenIdUrl(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);
        if (returnJson.getInt("errcode") != 0) {
            throw new SendCustomMsgException();
        }
        return 1;
    }


}
