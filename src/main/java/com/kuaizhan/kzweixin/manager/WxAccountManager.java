package com.kuaizhan.kzweixin.manager;

import com.kuaizhan.kzweixin.config.WxApiConfig;
import com.kuaizhan.kzweixin.constant.WxErrCode;
import com.kuaizhan.kzweixin.exception.weixin.*;
import com.kuaizhan.kzweixin.utils.HttpClientUtil;
import com.kuaizhan.kzweixin.exception.weixin.WxApiException;
import org.json.JSONObject;


/**
 * 微信账号管理相关的接口封装
 * Created by fangtianyu on 6/6/17.
 */
public class WxAccountManager {

    /**
     * 获取access token
     * @param appId 用户的app_id
     * @param appSecret 用户填写的app secret
     * @throws WxIPNotInWhitelistException: IP未设置为白名单
     * @throws WxInvalidAppSecretException: 错误的app secret
     * @throws WxApiException: 微信接口返回结果为null, 不能json序列化，或返回未知错误码
     * @return 获取的access token
     * */
    public static String getAccessToken(String appId, String appSecret) throws WxIPNotInWhitelistException,
            WxInvalidAppSecretException, WxApiException {
        String result = HttpClientUtil.get(WxApiConfig.getAccessTokenUrl(appId, appSecret));

        if (result == null) {
            throw new WxApiException("[WeiXin:getAccessToken] result is null");
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
            throw new WxApiException("[Weixin:getAccessToken] unexpected result:" + resultJson);
        }
    }
}
