package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.config.WxApiConfig;
import com.kuaizhan.kzweixin.config.ApplicationConfig;
import com.kuaizhan.kzweixin.cache.AuthCache;
import com.kuaizhan.kzweixin.exception.common.DecryptException;
import com.kuaizhan.kzweixin.exception.common.GetComponentAccessTokenFailed;
import com.kuaizhan.kzweixin.exception.common.XMLParseException;
import com.kuaizhan.kzweixin.exception.deprecated.system.*;
import com.kuaizhan.kzweixin.manager.WxThirdPartManager;
import com.kuaizhan.kzweixin.service.ThirdPartService;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.EncryptUtil;
import com.kuaizhan.kzweixin.utils.HttpClientUtil;
import com.kuaizhan.kzweixin.utils.weixin.AesException;
import com.kuaizhan.kzweixin.utils.weixin.WXBizMsgCrypt;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by liangjiateng on 2017/3/15.
 */
@Service("weixinAuthService")
public class ThirdPartServiceImpl implements ThirdPartService {

    public static final Logger logger = LoggerFactory.getLogger(ThirdPartServiceImpl.class);

    @Resource
    private AuthCache authCache;

    @Override
    public boolean checkMsg(String signature, String timestamp, String nonce) throws EncryptException {
        String[] arrTmp = {ApplicationConfig.WEIXIN_TOKEN, timestamp, nonce};
        Arrays.sort(arrTmp);
        StringBuilder sb = new StringBuilder();
        for (String anArrTmp : arrTmp) {
            sb.append(anArrTmp);
        }
        String pwd;
        try {
            pwd = EncryptUtil.sha1(sb.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException(e);
        }
        return pwd.equals(signature);
    }

    @Override
    public void refreshComponentVerifyTicket(String signature, String timestamp, String nonce, String postData) {
        //对消息进行解密
        String msg;
        logger.info("[WeiXin:ticket] ticket callback calling");
        try {
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(ApplicationConfig.WEIXIN_TOKEN,
                    ApplicationConfig.WEIXIN_AES_KEY,
                    ApplicationConfig.WEIXIN_APPID_THIRD);
            msg = wxBizMsgCrypt.decryptMsg(signature, timestamp, nonce, postData);
        } catch (AesException e) {
            throw new DecryptException("[WeiXin:getComponentVerifyTicket] decrypt failed", e);
        }
        //解析xml
        Document document;
        try {
            document = DocumentHelper.parseText(msg);
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
