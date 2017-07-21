package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.cache.AuthLoginCache;
import com.kuaizhan.kzweixin.cache.model.AuthLoginInfo;
import com.kuaizhan.kzweixin.config.ApplicationConfig;
import com.kuaizhan.kzweixin.entity.api.response.AccessTokenResponse;
import com.kuaizhan.kzweixin.entity.api.response.UserInfoResponse;
import com.kuaizhan.kzweixin.enums.AuthorizeScope;
import com.kuaizhan.kzweixin.manager.WxAuthorizeLoginManager;
import com.kuaizhan.kzweixin.service.AuthorizeLoginService;
import com.kuaizhan.kzweixin.service.WxThirdPartService;
import com.kuaizhan.kzweixin.utils.UrlUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Created by zixiong on 2017/7/13.
 */
@Service
public class AuthorizeLoginServiceImpl implements AuthorizeLoginService {

    @Resource
    private WxThirdPartService wxThirdPartService;
    @Resource
    private AuthLoginCache authLoginCache;

    // 微信授权完成后，回调的api
    private static final String AUTHORIZE_REDIRECT_URL = "/kzweixin/public/v1/authorize_redirect";

    @Override
    public String getAuthorizeLoginUrl(String appId, String redirectUrl, AuthorizeScope scope) {
        // 微信回调服务器的地址
        String authorizeRedirect = "http://" + ApplicationConfig.KZ_DOMAIN_OUTSIDE + AUTHORIZE_REDIRECT_URL +
                "?redirectUrl=" + UrlUtil.encode(redirectUrl);

        return "https://open.weixin.qq.com/connect/oauth2/authorize" +
                "?appid=" + appId +
                "&redirect_uri=" + UrlUtil.encode(authorizeRedirect) +
                "&response_type=code" +
                "&scope=" + scope.getValue() +
                "&component_appid=" + ApplicationConfig.WEIXIN_APPID_THIRD +
                "#wechat_redirect";
    }

    @Override
    public String getRedirectUrlWithToken(String appId, String code, String redirectUrl) {

        // 参数分隔符
        String paramSep = redirectUrl.contains("?")? "&" : "?";

        // 用户取消授权
        if (code == null) {
            return redirectUrl + paramSep + "status=0";
        }

        AccessTokenResponse accessToken = WxAuthorizeLoginManager.getAccessToken(appId, code,
                ApplicationConfig.WEIXIN_APPID_THIRD, wxThirdPartService.getComponentAccessToken());

        AuthLoginInfo authLoginInfo;
        // snsapi_base授权
        if (Objects.equals(accessToken.getScope(), AuthorizeScope.SNSAPI_BASE.getValue())) {
            authLoginInfo = new AuthLoginInfo();
            authLoginInfo.setAppid(appId);
            authLoginInfo.setOpenid(accessToken.getOpenId());

        // snsapi_userinfo授权
        } else {
            UserInfoResponse userInfo = WxAuthorizeLoginManager.getUserInfo(accessToken.getOpenId(),
                    accessToken.getAccessToken());
            authLoginInfo = userInfo.toAuthLogoinInfo();
            authLoginInfo.setAppid(appId);
        }
        String token = authLoginCache.setAuthLoginInfo(authLoginInfo);
        return redirectUrl + paramSep + "status=1&token=" + token;
    }

    @Override
    public AuthLoginInfo getAuthLoginInfoByToken(String token) {
        return authLoginCache.getAuthLoginInfo(token);
    }
}
