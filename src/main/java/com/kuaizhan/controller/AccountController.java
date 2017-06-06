package com.kuaizhan.controller;


import com.google.common.collect.ImmutableMap;
import com.kuaizhan.annotation.Validate;
import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.exception.deprecated.business.ParamException;
import com.kuaizhan.pojo.po.AccountPO;
import com.kuaizhan.pojo.po.UnbindPO;
import com.kuaizhan.pojo.vo.AccountVO;
import com.kuaizhan.pojo.vo.JsonResponse;
import com.kuaizhan.service.AccountService;

import com.kuaizhan.utils.PojoSwitcher;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
     * 解绑账户
     */
    @RequestMapping(value = "/account/unbind", method = RequestMethod.POST)
    public JsonResponse unbind(@Validate(key = "siteId") @RequestParam long siteId,
                               @Validate(key = "postData", path = "json/unbind-postdata-schema.json")
                               @RequestBody String postData) {
        AccountPO account = accountService.getAccountBySiteId(siteId);
        JSONObject jsonObject = new JSONObject(postData);
        int type = jsonObject.getInt("type");
        String text = jsonObject.getString("text");
        UnbindPO unbind = new UnbindPO();
        unbind.setUnbindText(text);
        unbind.setUnbindType(type);
        accountService.unbindAccount(account, unbind);
        return new JsonResponse(null);
    }

    /**
     * 修改app_secret
     */
    @RequestMapping(value = "/account", method = RequestMethod.PUT)
    public JsonResponse modifyAppSecret(@RequestParam long siteId, @RequestBody String postData) throws ParamException {
        String appSecret;
        try {
            JSONObject jsonObject = new JSONObject(postData);
            appSecret = jsonObject.getString("appSecret");
        } catch (Exception e) {
            throw new ParamException();
        }
        accountService.updateAppSecret(siteId, appSecret);
        return new JsonResponse(null);
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
