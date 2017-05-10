package com.kuaizhan.service.impl;

import com.kuaizhan.config.WxApiConfig;
import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.redis.RedisAuthDao;
import com.kuaizhan.exception.common.DecryptException;
import com.kuaizhan.exception.common.GetComponentAccessTokenFailed;
import com.kuaizhan.exception.common.RedisException;
import com.kuaizhan.exception.common.XMLParseException;
import com.kuaizhan.exception.deprecated.system.*;
import com.kuaizhan.pojo.DTO.AuthorizationInfoDTO;
import com.kuaizhan.pojo.DTO.AuthorizerInfoDTO;
import com.kuaizhan.service.WeixinAuthService;
import com.kuaizhan.utils.EncryptUtil;
import com.kuaizhan.utils.HttpClientUtil;
import com.kuaizhan.utils.JsonUtil;
import com.kuaizhan.utils.weixin.AesException;
import com.kuaizhan.utils.weixin.WXBizMsgCrypt;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by liangjiateng on 2017/3/15.
 */
@Service("weixinAuthService")
public class WeixinAuthServiceImpl implements WeixinAuthService {

    public static final Logger logger = Logger.getLogger(WeixinAuthServiceImpl.class);

    @Resource
    RedisAuthDao redisAuthDao;

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
        logger.info("[WeiXin:ticket] ticket callback calling.");
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
        logger.info("[WeiXin:ticket] get ticket:" + ticket);
        //缓存
        if (ticket != null) {
            redisAuthDao.setComponentVerifyTicket(ticket.getText());
        }
    }

    @Override
    public String getComponentAccessToken() {
        //TODO: component_access_token 直接存储json
        String ticket = redisAuthDao.getComponentVerifyTicket();
        if (ticket == null || "".equals(ticket)){
            throw new GetComponentAccessTokenFailed("[weixin:getComponentAccessToken] ticket is null");
        }

        String componentAccessToken = redisAuthDao.getComponentAccessToken();
        if (componentAccessToken == null) {
                //请求微信接口
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("component_appid", ApplicationConfig.WEIXIN_APPID_THIRD);
            jsonObject.put("component_appsecret", ApplicationConfig.WEIXIN_APP_SECRET_THIRD);
            jsonObject.put("component_verify_ticket", ticket);

            String result = HttpClientUtil.postJson(WxApiConfig.getComponentAccessTokenUrl(), jsonObject.toString());
            logger.info("[WeiXin:getComponentAccessToken] get componentAccessToken, params: " + jsonObject + " return: " + result);

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

            resultJson.put("expires_time", System.currentTimeMillis() / 1000 + 7100);
            //检查token是否一样
            redisAuthDao.setComponentAccessToken(resultJson.toString());
        }
        return componentAccessToken;
    }

    @Override
    public String getPreAuthCode(String componentAccessToken) throws RedisException, JsonParseException {
        //检查缓存
        try {
            String preAuthCode = redisAuthDao.getPreAuthCode();
            if (preAuthCode != null && ! "".equals(preAuthCode)) {
                return preAuthCode;
            }
        } catch (Exception e) {
            throw new RedisException(e);
        }
        //从微信调取
        String preAuthCode;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("component_appid", ApplicationConfig.WEIXIN_APPID_THIRD);
            String returnJson = HttpClientUtil.postJson(WxApiConfig.getPreAuthCodeUrl(componentAccessToken), jsonObject.toString());
            JSONObject result = new JSONObject(returnJson);
            preAuthCode = result.getString("pre_auth_code");
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
        //存缓存
        try {
            // 不相等则存
            String oldPreAuthCode = redisAuthDao.getPreAuthCode();
            if (!Objects.equals(oldPreAuthCode, preAuthCode)){
                redisAuthDao.setPreAuthCode(preAuthCode);
            }
        } catch (Exception e) {
            throw new RedisException(e);
        }
        return preAuthCode;
    }

    @Override
    public AuthorizationInfoDTO getAuthorizationInfo(String componentAccessToken, String componentAppId, String authCode) throws JsonParseException {
        AuthorizationInfoDTO authorizationInfoDTO;
        try {
            //请求
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("component_appid", componentAppId);
            jsonObject.put("authorization_code", authCode);
            String result = HttpClientUtil.postJson(WxApiConfig.getQueryAuthUrl(componentAccessToken), jsonObject.toString());
            // TODO: 此处有NPE问题
            JSONObject jsonObject1 = new JSONObject(result);
            result = jsonObject1.get("authorization_info").toString();
            //反序列化
            authorizationInfoDTO = JsonUtil.string2Bean(result, AuthorizationInfoDTO.class);
            authorizationInfoDTO.setFuncInfo(jsonObject1.getJSONObject("authorization_info").get("func_info").toString());
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
        return authorizationInfoDTO;
    }

    @Override
    public AuthorizationInfoDTO refreshAuthorizationInfo(String componentAccessToken, String componentAppId, String authorizerAppId, String authorizerRefreshToken) {
        AuthorizationInfoDTO authorizationInfoDTO;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("component_appid", componentAppId);
        jsonObject.put("authorizer_appid", authorizerAppId);
        jsonObject.put("authorizer_refresh_token", authorizerRefreshToken);
        String result = HttpClientUtil.postJson(WxApiConfig.getRefreshAuthUrl(componentAccessToken), jsonObject.toString());
        authorizationInfoDTO = JsonUtil.string2Bean(result, AuthorizationInfoDTO.class);
        return authorizationInfoDTO;
    }

    @Override
    public AuthorizerInfoDTO getAuthorizerInfo(String componentAccessToken, String componentAppId, String authorizerAppId) throws JsonParseException {
        AuthorizerInfoDTO authorizerInfoDTO;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("component_appid", componentAppId);
            jsonObject.put("authorizer_appid", authorizerAppId);
            String result = HttpClientUtil.postJson(WxApiConfig.getAuthorizerInfoUrl(componentAccessToken), jsonObject.toString());
            JSONObject jsonObject1 = new JSONObject(result);
            result = jsonObject1.get("authorizer_info").toString();
            authorizerInfoDTO = JsonUtil.string2Bean(result, AuthorizerInfoDTO.class);
            authorizerInfoDTO.setBusinessInfo(jsonObject1.getJSONObject("authorizer_info").get("business_info").toString());
            authorizerInfoDTO.setServiceTypeInfo(jsonObject1.getJSONObject("authorizer_info").get("service_type_info").toString());
            authorizerInfoDTO.setVerifyTypeInfo(jsonObject1.getJSONObject("authorizer_info").get("verify_type_info").toString());
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
        return authorizerInfoDTO;
    }
}
