package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.config.WxApiConfig;
import com.kuaizhan.kzweixin.config.ApplicationConfig;
import com.kuaizhan.kzweixin.cache.AuthCache;
import com.kuaizhan.kzweixin.exception.common.DecryptException;
import com.kuaizhan.kzweixin.exception.common.GetComponentAccessTokenFailed;
import com.kuaizhan.kzweixin.exception.common.XMLParseException;
import com.kuaizhan.kzweixin.exception.deprecated.system.*;
import com.kuaizhan.kzweixin.manager.WxAuthManager;
import com.kuaizhan.kzweixin.pojo.dto.AuthorizationInfoDTO;
import com.kuaizhan.kzweixin.service.WeixinAuthService;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.EncryptUtil;
import com.kuaizhan.kzweixin.utils.HttpClientUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
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
public class WeixinAuthServiceImpl implements WeixinAuthService {

    public static final Logger logger = LoggerFactory.getLogger(WeixinAuthServiceImpl.class);

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
    public void getComponentVerifyTicket(String signature, String timestamp, String nonce, String postData) {
        //对消息进行解密
        String msg;
        logger.info("[WeiXin:ticket] ticket callback calling");
        try {
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(ApplicationConfig.WEIXIN_TOKEN, ApplicationConfig.WEIXIN_AES_KEY, ApplicationConfig.WEIXIN_APPID_THIRD);
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
        //缓存
        if (ticket != null) {
            authCache.setComponentVerifyTicket(ticket.getText());
        }
    }

    @Override
    public String getComponentAccessToken() {
        //TODO: component_access_token 直接存储json
        String ticket = authCache.getComponentVerifyTicket();
        if (ticket == null || "".equals(ticket)){
            throw new GetComponentAccessTokenFailed("[weixin:getComponentAccessToken] ticket is null");
        }

        String componentAccessToken = authCache.getComponentAccessToken();
        if (componentAccessToken == null) {
                //请求微信接口
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("component_appid", ApplicationConfig.WEIXIN_APPID_THIRD);
            jsonObject.put("component_appsecret", ApplicationConfig.WEIXIN_APP_SECRET_THIRD);
            jsonObject.put("component_verify_ticket", ticket);

            String result = HttpClientUtil.postJson(WxApiConfig.getComponentAccessTokenUrl(), jsonObject.toString());
            logger.info("[WeiXin:getComponentAccessToken] get componentAccessToken, params:{} return:{}", jsonObject, result);

            if (result == null) {
                throw new GetComponentAccessTokenFailed("[weixin:getComponentAccessToken] result is null");
            }

            JSONObject resultJson;
            try {
                resultJson = new JSONObject(result);
                componentAccessToken = resultJson.getString("component_access_token");
            } catch (JSONException e) {
                throw new GetComponentAccessTokenFailed("[weixin:getComponentAccessToken] json exception, result:" + result, e);
            }

            resultJson.put("expires_time", DateUtil.curSeconds() + 7100);
            //检查token是否一样
            authCache.setComponentAccessToken(resultJson.toString());
        }
        return componentAccessToken;
    }

    @Override
    public String getPreAuthCode() {
        return WxAuthManager.getPreAuthCode(getComponentAccessToken());
    }

    @Override
    public AuthorizationInfoDTO refreshAuthorizationInfo(String componentAccessToken, String componentAppId, String authorizerAppId, String authorizerRefreshToken) {
        AuthorizationInfoDTO authorizationInfoDTO;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("component_appid", componentAppId);
        jsonObject.put("authorizer_appid", authorizerAppId);
        jsonObject.put("authorizer_refresh_token", authorizerRefreshToken);
        String result = HttpClientUtil.postJson(WxApiConfig.getRefreshAuthUrl(componentAccessToken), jsonObject.toString());
        // TODO: 这种写法，完全没有对返回数据做校验
        authorizationInfoDTO = JsonUtil.string2Bean(result, AuthorizationInfoDTO.class);
        return authorizationInfoDTO;
    }
}
