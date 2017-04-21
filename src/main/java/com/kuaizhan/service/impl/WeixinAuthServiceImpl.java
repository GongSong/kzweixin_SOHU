package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApiConfig;
import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.redis.RedisAuthDao;
import com.kuaizhan.exception.system.*;
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
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arrTmp.length; i++) {
            sb.append(arrTmp[i]);
        }
        String pwd = null;
        try {
            pwd = EncryptUtil.sha1(sb.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException(e);
        }
        if (pwd.equals(signature)) {
            return true;
        }
        return false;
    }

    @Override
    public void getComponentVerifyTicket(String signature, String timestamp, String nonce, String postData) throws DecryptException, XMLParseException, RedisException {
        //对消息进行解密
        String msg;
        try {
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(ApplicationConfig.WEIXIN_TOKEN, ApplicationConfig.WEIXIN_AES_KEY, ApplicationConfig.WEIXIN_APPID_THIRD);
            msg = wxBizMsgCrypt.decryptMsg(signature, timestamp, nonce, postData);
        } catch (AesException e) {
            throw new DecryptException(e);
        }
        //解析xml
        Document document;
        try {
            document = DocumentHelper.parseText(msg);
        } catch (DocumentException e) {
            throw new XMLParseException(e);
        }
        Element root = document.getRootElement();
        Element ticket = root.element("ComponentVerifyTicket");
        //缓存
        if (ticket != null) {
            try {
                redisAuthDao.setComponentVerifyTicket(ticket.getText());
            } catch (Exception e) {
                throw new RedisException(e);
            }
        }
    }

    @Override
    public String getComponentAccessToken() throws RedisException, JsonParseException {
        //TODO: component_access_token 直接存储json
        String ticket;
        String componentAccessToken;
        JSONObject result;
        try {
            //从redis拿componentAccessToken
            componentAccessToken = redisAuthDao.getComponentAccessToken();
            //从缓存中拿ticket
            ticket = redisAuthDao.getComponentVerifyTicket();
            if (ticket == null || "".equals(ticket)){
                logger.error("获取ticket失败, ticket:" + ticket);
            }
        } catch (Exception e) {
            throw new RedisException(e);
        }
        if (componentAccessToken == null) {
            try {
                //请求微信接口
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("component_appid", ApplicationConfig.WEIXIN_APPID_THIRD);
                jsonObject.put("component_appsecret", ApplicationConfig.WEIXIN_APP_SECRET_THIRD);
                jsonObject.put("component_verify_ticket", ticket);
                String returnJson = HttpClientUtil.postJson(ApiConfig.getComponentAccessTokenUrl(), jsonObject.toString());
                logger.info("[微信第三方平台] 获取componentAccessToken, params: " + jsonObject + " return: " + returnJson);
                result = new JSONObject(returnJson);
                result.put("expires_time", System.currentTimeMillis() / 1000 + 7100);
                componentAccessToken = result.getString("component_access_token");
            } catch (Exception e) {
                throw new JsonParseException(e);
            }
            try {
                //检查token是否一样
                redisAuthDao.setComponentAccessToken(result.toString());
            } catch (Exception e) {
                throw new RedisException(e);
            }
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
            String returnJson = HttpClientUtil.postJson(ApiConfig.getPreAuthCodeUrl(componentAccessToken), jsonObject.toString());
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
            String result = HttpClientUtil.postJson(ApiConfig.getQueryAuthUrl(componentAccessToken), jsonObject.toString());
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
    public AuthorizationInfoDTO refreshAuthorizationInfo(String componentAccessToken, String componentAppId, String authorizerAppId, String authorizerRefreshToken) throws JsonParseException {
        AuthorizationInfoDTO authorizationInfoDTO;
        try {
            // TODO: 获取token失败时，应该抛异常。
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("component_appid", componentAppId);
            jsonObject.put("authorizer_appid", authorizerAppId);
            jsonObject.put("authorizer_refresh_token", authorizerRefreshToken);
            String result = HttpClientUtil.postJson(ApiConfig.getRefreshAuthUrl(componentAccessToken), jsonObject.toString());
            authorizationInfoDTO = JsonUtil.string2Bean(result, AuthorizationInfoDTO.class);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
        return authorizationInfoDTO;
    }

    @Override
    public AuthorizerInfoDTO getAuthorizerInfo(String componentAccessToken, String componentAppId, String authorizerAppId) throws JsonParseException {
        AuthorizerInfoDTO authorizerInfoDTO;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("component_appid", componentAppId);
            jsonObject.put("authorizer_appid", authorizerAppId);
            String result = HttpClientUtil.postJson(ApiConfig.getAuthorizerInfoUrl(componentAccessToken), jsonObject.toString());
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
