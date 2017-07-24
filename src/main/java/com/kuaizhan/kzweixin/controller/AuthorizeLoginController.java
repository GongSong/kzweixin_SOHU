package com.kuaizhan.kzweixin.controller;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.cache.model.AuthLoginInfo;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.enums.AuthorizeScope;
import com.kuaizhan.kzweixin.service.AuthorizeLoginService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;

/**
 * Created by zixiong on 2017/7/13.
 */
@RestController
public class AuthorizeLoginController extends BaseController {

    @Resource
    private AuthorizeLoginService authorizeLoginService;

    @RequestMapping(value = "public/v1/authorize_login")
    public RedirectView authorizeLogin(@RequestParam String appid,
                                       @RequestParam String redirectUrl,
                                       @RequestParam String scope) {

        String url = authorizeLoginService.getAuthorizeLoginUrl(appid, redirectUrl, AuthorizeScope.fromValue(scope));
        return new RedirectView(url);
    }

    @RequestMapping(value = "public/v1/authorize_redirect")
    public RedirectView authorizeRedirect(@RequestParam String redirectUrl, @RequestParam String code, @RequestParam String appid) {
        String redirectUrlWithToken = authorizeLoginService.getRedirectUrlWithToken(appid, code, redirectUrl);
        return new RedirectView(redirectUrlWithToken);
    }

    @RequestMapping(value = "public/v1/authorize_test")
    public JsonResponse authorizeTest(@RequestParam String openid, @RequestParam String nickname) {
        return new JsonResponse(ImmutableMap.of("openid", openid, "nickname", nickname));
    }

    @RequestMapping(value = "v1/authorize_user_info", method = RequestMethod.GET)
    public JsonResponse getUserInfo(@RequestParam String token) {
        AuthLoginInfo authLoginInfo = authorizeLoginService.getAuthLoginInfoByToken(token);
        if (authLoginInfo == null) {
            return new JsonResponse(null);
        }
        return new JsonResponse(ImmutableMap.of("appid", authLoginInfo.getAppid(), "userInfo", authLoginInfo));
    }
}
