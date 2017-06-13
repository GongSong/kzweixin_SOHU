package com.kuaizhan.controller;


import com.google.common.collect.ImmutableMap;
import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.controller.param.AddAccountParam;
import com.kuaizhan.pojo.po.AccountPO;
import com.kuaizhan.controller.vo.AccountSettingVO;
import com.kuaizhan.controller.vo.AccountVO;
import com.kuaizhan.controller.vo.JsonResponse;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.controller.param.UpdateAppSecretParam;
import com.kuaizhan.utils.PojoSwitcher;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

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
     * 获取绑定url
     * @param userId 绑定公众号的userId
     * @param siteId 是否绑定在某个站点上
     */
    @RequestMapping(value = "/account/bind_url", method = RequestMethod.GET)
    public JsonResponse getBindUrl(@RequestParam long userId, @RequestParam(required = false) Long siteId) {
        String bindUrl = accountService.getBindUrl(userId, siteId);
        return new JsonResponse(ImmutableMap.of("url", bindUrl));
    }

    /**
     * 新增一个绑定(授权者)
     */
    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public JsonResponse addBindAccount(@Valid @RequestBody AddAccountParam param) {
        accountService.bindAccount(param.getUserId(), param.getAuthCode(), param.getSiteId());
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 修改app_secret
     */
    @RequestMapping(value = "/accounts/{weixinAppid}/app_secret", method = RequestMethod.PUT)
    public JsonResponse updateAppSecret(@PathVariable("weixinAppid") long weixinAppid, @Valid @RequestBody UpdateAppSecretParam param) {
        accountService.updateAppSecret(weixinAppid, param.getAppSecret());
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
