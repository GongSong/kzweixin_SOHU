package com.kuaizhan.kzweixin.manager;

import com.kuaizhan.kzweixin.config.ApplicationConfig;
import com.kuaizhan.kzweixin.config.WxApiConfig;
import com.kuaizhan.kzweixin.exception.weixin.WxApiException;
import com.kuaizhan.kzweixin.entity.account.AuthorizationInfoDTO;
import com.kuaizhan.kzweixin.entity.account.AuthorizerInfoDTO;
import com.kuaizhan.kzweixin.utils.HttpClientUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 第三方平台相关api
 * Created by zixiong on 2017/6/6.
 */
public class WxThirdPartManager {

    public static final Logger logger = LoggerFactory.getLogger(WxThirdPartManager.class);

    /**
     * 获取ComponentAccessToken
     */
    public static JSONObject getComponentAccessToken(String componentAppid, String componentAppSecret, String ticket) {
        Map<String, String> param = new HashMap<>();
        param.put("component_appid", componentAppid);
        param.put("component_appsecret", componentAppSecret);
        param.put("component_verify_ticket", ticket);
        String paramStr = JsonUtil.bean2String(param);

        String result = HttpClientUtil.postJson(WxApiConfig.getComponentAccessTokenUrl(), paramStr);
        logger.info("[WeiXin:getComponentAccessToken] get componentAccessToken, params:{} return:{}", paramStr, result);

        if (result == null) {
            throw new WxApiException("[weixin:getComponentAccessToken] result is null");
        }

        JSONObject resultJson = new JSONObject(result);
        if (resultJson.optInt("errcode") != 0 || !resultJson.has("component_access_token")) {
            throw new WxApiException("[weixin:getComponentAccessToken] unexpected result:" + result);
        }

        return resultJson;
    }

    /**
     * 获取预授权码
     * @throws WxApiException: 未知异常
     */
    public static String getPreAuthCode(String componentAccessToken) throws WxApiException {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("component_appid", ApplicationConfig.WEIXIN_APPID_THIRD);
        String paramStr = JsonUtil.bean2String(paramMap);

        String result = HttpClientUtil.postJson(WxApiConfig.getPreAuthCodeUrl(componentAccessToken), paramStr);
        if (result == null) {
            throw new WxApiException("[Weixin:getPreAuthCode] result is null");
        }

        JSONObject resultJson = new JSONObject(result);
        int errCode = resultJson.optInt("errcode");
        if (errCode != 0) {
            throw new WxApiException("[Weixin:getPreAuthCode] unexpected result:" + resultJson);
        }
        return resultJson.getString("pre_auth_code");
    }

    /**
     * 获取授权信息
     * @throws WxApiException: 未知调用错误
     */
    public static AuthorizationInfoDTO getAuthorizationInfo(String componentAppid, String componentAccessToken, String authCode) {
        Map<String, String> param = new HashMap<>();
        param.put("component_appid", componentAppid);
        param.put("authorization_code", authCode);

        String url = WxApiConfig.getQueryAuthUrl(componentAccessToken);
        String result = HttpClientUtil.postJson(url, JsonUtil.bean2String(param));

        if (result == null) {
            throw new WxApiException("[Weixin:getAuthorizationInfo] result is null");
        }
        JSONObject resultJson = new JSONObject(result);
        if (resultJson.optInt("errcode") != 0) {
            throw new WxApiException("[Weixin:getAuthorizationInfo] unexpected result:" + result);
        }
        return JsonUtil.string2Bean(result, AuthorizationInfoDTO.class);
    }

    /**
     * 获取授权公众号信息
     */
    public static AuthorizerInfoDTO getAuthorizerInfo(String componentAppid, String componentAccessToken, String appid) {
        Map<String, String> param = new HashMap<>();
        param.put("component_appid", componentAppid);
        param.put("authorizer_appid", appid);

        String result = HttpClientUtil.postJson(WxApiConfig.getAuthorizerInfoUrl(componentAccessToken), JsonUtil.bean2String(param));

        if (result == null) {
            throw new WxApiException("[Weixin:getAuthorizerInfo] result is null");
        }

        JSONObject resultJson = new JSONObject(result);
        if (resultJson.optInt("errcode") != 0) {
            throw new WxApiException("[Weixin:getAuthorizerInfo] unexpected result:" + result);
        }

       return JsonUtil.string2Bean(result, AuthorizerInfoDTO.class);
    }
}
