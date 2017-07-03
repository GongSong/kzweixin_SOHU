package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.config.ApplicationConfig;
import com.kuaizhan.kzweixin.cache.AuthCache;
import com.kuaizhan.kzweixin.exception.common.DecryptException;
import com.kuaizhan.kzweixin.exception.common.EncryptException;
import com.kuaizhan.kzweixin.exception.common.GetComponentAccessTokenFailed;
import com.kuaizhan.kzweixin.exception.common.XMLParseException;
import com.kuaizhan.kzweixin.manager.WxThirdPartManager;
import com.kuaizhan.kzweixin.service.WxThirdPartService;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.weixin.AesException;
import com.kuaizhan.kzweixin.utils.weixin.WXBizMsgCrypt;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by liangjiateng on 2017/3/15.
 */
@Service
public class WxThirdPartServiceImpl implements WxThirdPartService {

    public static final Logger logger = LoggerFactory.getLogger(WxThirdPartServiceImpl.class);

    @Resource
    private AuthCache authCache;

    @Override
    public String decryptMsg(String signature, String timestamp, String nonce, String content) {
        try {
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(ApplicationConfig.WEIXIN_TOKEN,
                    ApplicationConfig.WEIXIN_AES_KEY,
                    ApplicationConfig.WEIXIN_APPID_THIRD);
            return wxBizMsgCrypt.decryptMsg(signature, timestamp, nonce, content);
        } catch (AesException e) {
            throw new DecryptException("[Weixin:decryptMsg] decrypt failed," +
                    " signature:" + signature +
                    " timestamp:" + timestamp +
                    " nonce:" + nonce +
                    " content:" + content, e);
        }
    }

    @Override
    public String encryptMsg(String content) {
        try{
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(ApplicationConfig.WEIXIN_TOKEN,
                    ApplicationConfig.WEIXIN_AES_KEY,
                    ApplicationConfig.WEIXIN_APPID_THIRD);

            String timestamp = Long.toString(DateUtil.curSeconds());
            String nonce = Long.toString(ThreadLocalRandom.current().nextLong(1000000000L, 9999999999L));
            return wxBizMsgCrypt.encryptMsg(content, timestamp, nonce);
        } catch (AesException e) {
            throw new EncryptException("[Weixin:encryMsg] encrypt failed, content:" + content, e);
        }
    }

    @Override
    public void refreshComponentVerifyTicket(String xmlStr) {
        //对消息进行解密
        logger.info("[WeiXin:ticket] ticket callback calling");
        //解析xml
        Document document;
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            throw new XMLParseException("[WeiXin:getComponentVerifyTicket] xml parse failed", e);
        }
        Element root = document.getRootElement();
        Element ticket = root.element("ComponentVerifyTicket");
        logger.info("[WeiXin:ticket] get ticket:{}", ticket);
        // 缓存
        authCache.setComponentVerifyTicket(ticket.getText());
    }

    @Override
    public String getComponentAccessToken() {
        String componentAccessToken = authCache.getComponentAccessToken();
        if (componentAccessToken != null) {
            return componentAccessToken;
        }

        String ticket = authCache.getComponentVerifyTicket();
        if (ticket == null || "".equals(ticket)){
            throw new GetComponentAccessTokenFailed("[weixin:getComponentAccessToken] ticket is null");
        }
        JSONObject resultJson = WxThirdPartManager.getComponentAccessToken(
                ApplicationConfig.WEIXIN_APPID_THIRD,
                ApplicationConfig.WEIXIN_APP_SECRET_THIRD,
                ticket);

        // 缓存， 这种做法，完全是为了兼容php
        int expiresIn = resultJson.getInt("expires_in");
        resultJson.put("expires_time", DateUtil.curSeconds() + expiresIn - 100);
        authCache.setComponentAccessToken(resultJson.toString());

    return resultJson.getString("component_access_token");
    }
}
