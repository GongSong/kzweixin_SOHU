package com.kuaizhan.controller;


import com.kuaizhan.config.ApiConfig;
import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.exception.system.*;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DTO.AuthorizationInfoDTO;
import com.kuaizhan.pojo.DTO.AuthorizerInfoDTO;
import com.kuaizhan.pojo.VO.JsonResponse;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.WeixinAuthService;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 微信回调入口
 * Created by Mr.Jadyn on 2017/1/11.
 */
@RestController
@RequestMapping(value = ApplicationConfig.VERSION + "/weixin")
public class WeixinController extends BaseController{

    @Resource
    WeixinAuthService weixinAuthService;
    @Resource
    AccountService accountService;

    /**
     * 获取微信推送的component_verify_ticket
     *
     * @return
     */
    @RequestMapping(value = "/auth/callback", method = RequestMethod.POST)
    public String receiver(@RequestParam("msg_signature") String signature, @RequestParam String timestamp, @RequestParam String nonce, @RequestBody String postData) throws XMLParseException, DecryptException, RedisException {
        weixinAuthService.getComponentVerifyTicket(signature, timestamp, nonce, postData);
        return "success";
    }

    /**
     * 获取授权页url
     *
     * @return
     */
    @RequestMapping(value = "/authpage/{siteId}", method = RequestMethod.GET, produces = "application/json")
    public JsonResponse getAuthPage(@PathVariable long siteId) throws RedisException, JsonParseException {
        String componentAccessToken = weixinAuthService.getComponentAccessToken();
        String preAuthCode = weixinAuthService.getPreAuthCode(componentAccessToken);
        String redirectUrl = ApplicationConfig.API_PREFIX + ApplicationConfig.VERSION + "/weixin/authcode/callback?siteId=" + siteId;
        String authPageUrl = ApiConfig.getAuthEntranceUrl(preAuthCode, redirectUrl);
        return new JsonResponse(authPageUrl);
    }

    /**
     * 获取authCode和expires
     *
     * @return
     */
    @RequestMapping(value = "/authcode/callback", method = RequestMethod.GET)
    public String getAuthCode(@RequestParam("auth_code") String authCode, @RequestParam("expires_in") String expire, @RequestParam long siteId) throws RedisException, JsonParseException, DaoException {

        String componentAccessToken = weixinAuthService.getComponentAccessToken();
        AuthorizationInfoDTO authorizationInfoDTO = weixinAuthService.getAuthorizationInfo(componentAccessToken, ApplicationConfig.WEIXIN_APPID_THIRD, authCode);
        if (authorizationInfoDTO != null) {
            AuthorizerInfoDTO authorizerInfoDTO = weixinAuthService.getAuthorizerInfo(componentAccessToken, ApplicationConfig.WEIXIN_APPID_THIRD, authorizationInfoDTO.getAppId());
            //装配数据
            AccountDO account = new AccountDO();
            account.setSiteId(siteId);
            account.setAppId(authorizationInfoDTO.getAppId());
            account.setAppSecret("");
            account.setAccessToken(authorizationInfoDTO.getAccessToken());
            account.setExpiresTime(System.currentTimeMillis() / 1000 + 7200);
            account.setRefreshToken(authorizationInfoDTO.getRefreshToken());
            account.setFuncInfoJson(authorizationInfoDTO.getFuncInfo());
            if (authorizerInfoDTO != null) {
                account.setMenuJson("");
                account.setNickName(authorizerInfoDTO.getNickName());
                account.setHeadImg(authorizerInfoDTO.getHeadImg());
                JSONObject jsonObject = new JSONObject(authorizerInfoDTO.getServiceTypeInfo());
                account.setServiceType(jsonObject.getInt("id"));
                jsonObject = new JSONObject(authorizerInfoDTO.getVerifyTypeInfo());
                account.setVerifyType(jsonObject.getInt("id"));
                account.setUserName(authorizerInfoDTO.getUsername());
                account.setBusinessInfoJson(authorizerInfoDTO.getBusinessInfo());
                account.setAlias(authorizerInfoDTO.getAlias());
                account.setQrcodeUrlKz("");
                account.setPreviewOpenId("");
                account.setQrcodeUrl(authorizerInfoDTO.getQrcodeUrl());
                account.setUnbindTime(0L);
                account.setInterestJson("[\"0\",\"0\",\"0\",\"0\",\"0\",\"0\"]");
                account.setAdvancedFuncInfoJson("{\"open_login\":0,\"open_share\":0}");
            }
            accountService.bindAccount(account);
        }
        return "success";
    }
}
