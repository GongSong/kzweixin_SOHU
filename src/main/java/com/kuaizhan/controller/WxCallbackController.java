package com.kuaizhan.controller;


import com.google.common.collect.ImmutableMap;
import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.exception.common.DaoException;
import com.kuaizhan.exception.common.RedisException;
import com.kuaizhan.exception.common.XMLParseException;
import com.kuaizhan.exception.deprecated.system.*;
import com.kuaizhan.pojo.po.AccountPO;
import com.kuaizhan.pojo.dto.AuthorizationInfoDTO;
import com.kuaizhan.pojo.dto.AuthorizerInfoDTO;
import com.kuaizhan.pojo.vo.JsonResponse;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.WeixinAuthService;
import com.kuaizhan.service.WeixinMsgService;
import com.kuaizhan.utils.DateUtil;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 微信回调接口
 * Created by Mr.Jadyn on 2017/1/11.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION + "/weixin")
public class WxCallbackController extends BaseController {

    @Resource
    private WeixinAuthService weixinAuthService;
    @Resource
    private AccountService accountService;
    @Resource
    private WeixinMsgService weixinMsgService;

    /**
     * 获取微信推送的component_verify_ticket
     */
    @RequestMapping(value = "/auth/callback", method = RequestMethod.POST)
    public String receiver(@RequestParam("msg_signature") String signature, @RequestParam String timestamp, @RequestParam String nonce, @RequestBody String postData) {
        weixinAuthService.getComponentVerifyTicket(signature, timestamp, nonce, postData);
        return "success";
    }


    /**
     * 微信消息推送
     */
    @RequestMapping(value = "/msgs/{appId}/callback", method = RequestMethod.POST, produces = "application/xml;charset=UTF-8")
    public String weixinPush(@PathVariable String appId, @RequestParam("msg_signature") String signature, @RequestParam String timestamp, @RequestParam String nonce, @RequestBody(required = false) String postData) throws EncryptException, DaoException, XMLParseException, RedisException {
        //检查消息是否来自微信
        return weixinMsgService.handleWeixinPushMsg(appId, signature, timestamp, nonce, postData);
    }


    /**
     * 获取authCode和expires 账号绑定
     */
    @RequestMapping(value = "/authcode/callback", method = RequestMethod.GET)
    public JsonResponse getAuthCode(@RequestParam("auth_code") String authCode, @RequestParam("expires_in") String expire, @RequestParam long siteId) throws RedisException, JsonParseException, DaoException {
        //TODO:绑定后数据同步 粉丝数据 引导关注 模板消息 rambitmq
        String componentAccessToken = weixinAuthService.getComponentAccessToken();
        AuthorizationInfoDTO authorizationInfoDTO = weixinAuthService.getAuthorizationInfo(componentAccessToken, ApplicationConfig.WEIXIN_APPID_THIRD, authCode);
        if (authorizationInfoDTO != null) {
            AuthorizerInfoDTO authorizerInfoDTO = weixinAuthService.getAuthorizerInfo(componentAccessToken, ApplicationConfig.WEIXIN_APPID_THIRD, authorizationInfoDTO.getAppId());
            //装配数据
            AccountPO account = new AccountPO();
            account.setSiteId(siteId);
            account.setAppId(authorizationInfoDTO.getAppId());
            account.setAppSecret("");
            account.setAccessToken(authorizationInfoDTO.getAccessToken());
            account.setExpiresTime((long) (DateUtil.curSeconds() + authorizationInfoDTO.getExpiresIn()));
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
        return new JsonResponse(ImmutableMap.of());
    }


}
