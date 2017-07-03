package com.kuaizhan.kzweixin.manager;

import com.kuaizhan.kzweixin.config.WxApiConfig;
import com.kuaizhan.kzweixin.constant.WxErrCode;
import com.kuaizhan.kzweixin.entity.account.AccessTokenDTO;
import com.kuaizhan.kzweixin.exception.weixin.*;
import com.kuaizhan.kzweixin.utils.HttpClientUtil;
import com.kuaizhan.kzweixin.exception.weixin.WxApiException;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 微信账号管理相关的接口封装
 * Created by fangtianyu on 6/6/17.
 */
public class WxAccountManager {

    /**
     * 刷新用户的accessToken
     */
    public static AccessTokenDTO refreshAccessToken(String componentAppId, String componentAccessToken,
                                                    String appId, String refreshToken) {
        Map<String, String> param = new HashMap<>();
        param.put("component_appid", componentAppId);
        param.put("authorizer_appid", appId);
        param.put("authorizer_refresh_token", refreshToken);

        String result = HttpClientUtil.postJson(WxApiConfig.getRefreshAuthUrl(componentAccessToken),
                JsonUtil.bean2String(param));

        if (result == null) {
            throw new WxApiException("[WeiXin:refreshAccessToken] result is null");
        }

        JSONObject resultJson = new JSONObject(result);
        if (resultJson.optInt("errcode") != 0) {
            throw new WxApiException("[Weixin:refreshAccessToken] unexpected result:" + resultJson);
        }

        return JsonUtil.string2Bean(result, AccessTokenDTO.class);
    }

    /**
     * 根据appId和appSecret获取access token
     * 此accessToken与公众号授权给第三方平台的accessToken不同
     * @throws WxIPNotInWhitelistException: IP未设置为白名单
     * @throws WxInvalidAppSecretException: 错误的app secret
     * @throws WxApiException: 微信接口返回结果为null, 不能json序列化，或返回未知错误码
     * @return 获取的access token
     * */
    public static String getUserAccessToken(String appId, String appSecret) throws WxIPNotInWhitelistException,
            WxInvalidAppSecretException, WxApiException {
        String result = HttpClientUtil.get(WxApiConfig.getAccessTokenUrl(appId, appSecret));

        if (result == null) {
            throw new WxApiException("[WeiXin:getUserAccessToken] result is null");
        }

        JSONObject resultJson = new JSONObject(result);
        String accessToken = resultJson.optString("access_token");

        if (!("".equals(accessToken))) {
            return accessToken;
        }

        int errCode = resultJson.optInt("errcode");

        if (errCode == WxErrCode.IP_NOT_IN_WHITELIST) {
            throw new WxIPNotInWhitelistException();
        } else if (errCode == WxErrCode.INVALID_APP_SECRET_1 || errCode == WxErrCode.INVALID_APP_SECRET_2) {
            throw new WxInvalidAppSecretException();
        } else {
            throw new WxApiException("[Weixin:getUserAccessToken] unexpected result:" + resultJson);
        }
    }
}
