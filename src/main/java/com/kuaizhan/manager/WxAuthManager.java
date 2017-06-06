package com.kuaizhan.manager;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.config.WxApiConfig;
import com.kuaizhan.exception.weixin.WxApiException;
import com.kuaizhan.utils.HttpClientUtil;
import com.kuaizhan.utils.JsonUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 第三方平台授权相关api
 * Created by zixiong on 2017/6/6.
 */
public class WxAuthManager {

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
}
