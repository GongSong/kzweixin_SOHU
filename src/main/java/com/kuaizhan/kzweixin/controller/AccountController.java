package com.kuaizhan.kzweixin.controller;


import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.controller.param.*;
import com.kuaizhan.kzweixin.dao.po.auto.AccountPO;
import com.kuaizhan.kzweixin.controller.vo.AccountSettingVO;
import com.kuaizhan.kzweixin.controller.vo.AccountVO;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.utils.PojoSwitcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 账号模块接口
 * Created by Mr.Jadyn on 2017/1/25.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION, produces = "application/json")
public class AccountController extends BaseController {
    @Resource
    private AccountService accountService;

    /**
     * 根据weixinAppid获取账号信息
     */
    @RequestMapping(value = "/accounts/{weixinAppid}", method = RequestMethod.GET)
    public JsonResponse getAccountInfo(@PathVariable long weixinAppid) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        AccountVO accountVO = PojoSwitcher.accountPOToVO(accountPO);
        return new JsonResponse(accountVO);
    }

    /**
     * 根据weixinAppid获取账号设置信息
     */
    @RequestMapping(value = "/accounts/{weixinAppid}/settings", method = RequestMethod.GET)
    public JsonResponse getAccountSettings(@PathVariable long weixinAppid) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        AccountSettingVO settingVO = PojoSwitcher.toAccountSettingVO(accountPO);
        return new JsonResponse(settingVO);
    }

    /**
     * 根据siteId获取绑定的微信公众号信息
     */
    @RequestMapping(value = "/accounts/site/{siteId}", method = RequestMethod.GET)
    public JsonResponse getAccountInfoBySiteId(@PathVariable long siteId) {
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        AccountVO accountVO = PojoSwitcher.accountPOToVO(accountPO);
        return new JsonResponse(accountVO);
    }

    /**
     * 新增绑定，跳转到微信服务器
     */
    @RequestMapping(value = "/account/binds", method = RequestMethod.POST)
    public RedirectView getBindUrl(@Valid @RequestBody BindParam param) {
        String bindUrl = accountService.getBindUrl(param.getUserId(), param.getSiteId(), param.getRedirectUrl());
        return new RedirectView(bindUrl);
    }

    @RequestMapping(value = "/account/bind_url", method = RequestMethod.GET)
    public JsonResponse addBindAccount(@RequestParam Long userId,
                                       @RequestParam(required = false) Long siteId,
                                       @RequestParam String redirectUrl ) {
        String bindUrl = accountService.getBindUrl(userId, siteId, redirectUrl);
        return new JsonResponse(ImmutableMap.of("url", bindUrl));
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public JsonResponse getAccounts(@RequestParam long userId) {
        List<AccountPO> accountPOS = accountService.getAccounts(userId);
        List<AccountVO> accountVOS = new ArrayList<>();

        for (AccountPO accountPO: accountPOS) {
            accountVOS.add(PojoSwitcher.accountPOToVO(accountPO));
        }
        return new JsonResponse(ImmutableMap.of("accounts", accountVOS));
    }

    /**
     * 修改app_secret
     */
    @RequestMapping(value = "/accounts/{weixinAppid}/app_secret", method = RequestMethod.PUT)
    public JsonResponse updateAppSecret(@PathVariable("weixinAppid") long weixinAppid, @Valid @RequestBody UpdateAppSecretParam param) {
        accountService.updateAppSecret(weixinAppid, param.getAppSecret());
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 修改用户自定义分享开启／关闭状态
     * */
    @RequestMapping(value = "/accounts/{weixinAppid}/customize_share", method = RequestMethod.PUT)
    public JsonResponse updateCustomizeShare(@PathVariable("weixinAppid") long weixinAppid, @Valid @RequestBody UpdateOpenShareParam param) {
        accountService.updateCustomizeShare(weixinAppid, param.getOpenShare());
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 修改服务号授权登录开启／关闭状态
     * */
    @RequestMapping(value = "/accounts/{weixinAppid}/authorize_login", method = RequestMethod.PUT)
    public JsonResponse updateAuthLogin(@PathVariable("weixinAppid") long weixinAppid, @Valid @RequestBody UpdateAuthLoginParam param) {
        accountService.updateAuthLogin(weixinAppid, param.getOpenLogin());
        return new JsonResponse(ImmutableMap.of());
    }

    /* --------------- deprecated ---------------- */

    /**
     * 因为接口设计风格而废弃的接口
     * 公众号独立后可以删除
     */
    @Deprecated
    @RequestMapping(value = "/account/site_id", method = RequestMethod.GET)
    public JsonResponse getAccountInfoBySiteIdDeprecated(@RequestParam long siteId) {
        return getAccountInfoBySiteId(siteId);
    }
}
