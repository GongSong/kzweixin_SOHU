package com.kuaizhan.kzweixin.controller;

import com.kuaizhan.kzweixin.enums.AuthorizeScope;
import com.kuaizhan.kzweixin.service.AuthorizeLoginService;
import org.springframework.web.bind.annotation.RequestMapping;
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
        String redirectUrlWithUserInfo = authorizeLoginService.getRedirectUrlWithUserInfo(appid, code, redirectUrl);
        return new RedirectView(redirectUrlWithUserInfo);
    }

    @RequestMapping(value = "public/v1/authorize_test")
    public String authorizeTest(@RequestParam String openid, @RequestParam String nickname) {
        return "appid: " + openid + " nickname: " + nickname;
    }
}
