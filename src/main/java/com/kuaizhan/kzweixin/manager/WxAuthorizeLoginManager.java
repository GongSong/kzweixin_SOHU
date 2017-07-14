package com.kuaizhan.kzweixin.manager;

import com.kuaizhan.kzweixin.config.WxApiConfig;
import com.kuaizhan.kzweixin.entity.api.response.AccessTokenResponse;
import com.kuaizhan.kzweixin.entity.api.response.UserInfoResponse;
import com.kuaizhan.kzweixin.exception.weixin.WxApiException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * 授权登录相关微信接口封装
 * Created by zixiong on 2017/7/14.
 */
public class WxAuthorizeLoginManager {

    /**
     * 根据code换取accessToken
     */
    public static AccessTokenResponse getAccessToken(String appid, String code, String componentAppid, String componentAccessToken) {

        String url = WxApiConfig.DOMAIN_WEIXIN_API + WxApiConfig.WEIXIN_AUTHORIZE_ACCESS_TOKEN +
                "?appid=" + appid +
                "&code=" + code +
                "&grant_type=authorization_code" +
                "&component_appid=" + componentAppid +
                "&component_access_token=" + componentAccessToken;

        HttpResponse<AccessTokenResponse> httpResponse;
        try {
            httpResponse = Unirest.get(url).asObject(AccessTokenResponse.class);
        } catch (UnirestException e) {
            throw new WxApiException("[Wx:AuthorizeLogin] getAccessToken failed", e);
        }
        AccessTokenResponse response = httpResponse.getBody();

        if (response.getErrcode() != 0) {
            throw new WxApiException("[Wx:AuthorizeLogin] unexpected response:" + response);
        }

        return response;
    }

    public static UserInfoResponse getUserInfo(String openid, String accessToken) {
        String url = WxApiConfig.DOMAIN_WEIXIN_API + WxApiConfig.WEIXIN_AUTHORIZE_USER_INFO +
                "?access_token=" + accessToken + "&openid=" + openid + "&lang=zh_CN";

        HttpResponse<UserInfoResponse> httpResponse;
        try {
            httpResponse = Unirest.get(url).asObject(UserInfoResponse.class);
        } catch (UnirestException e) {
            throw new WxApiException("[Wx:AuthorizeLogin] getUserInfo failed", e);
        }
        UserInfoResponse response = httpResponse.getBody();

        if (response.getErrcode() != 0) {
            throw new WxApiException("[Wx:AuthorizeLogin] unexpected response:" + response);
        }

        return response;
    }
}
