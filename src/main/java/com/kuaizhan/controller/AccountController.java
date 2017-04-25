package com.kuaizhan.controller;


import com.kuaizhan.annotation.Validate;
import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.exception.business.AccountNotExistException;
import com.kuaizhan.exception.business.ParamException;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.JsonParseException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DO.UnbindDO;
import com.kuaizhan.pojo.VO.AccountVO;
import com.kuaizhan.pojo.VO.JsonResponse;
import com.kuaizhan.service.AccountService;

import com.kuaizhan.utils.JsonUtil;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * 账号controller
 * Created by Mr.Jadyn on 2017/1/25.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION, produces = "application/json")
public class AccountController extends BaseController {
    @Resource
    AccountService accountService;

    /**
     * 获取账户信息
     *
     * @return
     */
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public JsonResponse getAccountInfo(@RequestParam long weixinAppid) throws RedisException, DaoException, AccountNotExistException, IOException, JsonParseException {
        AccountDO accountDO = accountService.getAccountByWeixinAppId(weixinAppid);
        AccountVO accountVO = new AccountVO();
        accountVO.setWeixinAppid(accountDO.getWeixinAppId());
        accountVO.setAppSecret(accountDO.getAppSecret());
        accountVO.setHeadImg(accountDO.getHeadImg());
        List<String> list = JsonUtil.string2List(accountDO.getInterestJson(), String.class);
        accountVO.setInterest(list);
        accountVO.setName(accountDO.getNickName());
        accountVO.setQrcode(accountDO.getQrcodeUrl());
        accountVO.setServiceType(accountDO.getServiceType());
        accountVO.setVerifyType(accountDO.getVerifyType());
        return new JsonResponse(accountVO);
    }

    @RequestMapping(value = "/account/site_id", method = RequestMethod.GET)
    public JsonResponse getAccountInfoBySiteId(@RequestParam long siteId) throws RedisException, DaoException, AccountNotExistException, IOException, JsonParseException {
        AccountDO accountDO = accountService.getAccountBySiteId(siteId);
        AccountVO accountVO = new AccountVO();
        accountVO.setWeixinAppid(accountDO.getWeixinAppId());
        accountVO.setAppSecret(accountDO.getAppSecret());
        accountVO.setHeadImg(accountDO.getHeadImg());
        List<String> list = JsonUtil.string2List(accountDO.getInterestJson(), String.class);
        accountVO.setInterest(list);
        accountVO.setName(accountDO.getNickName());
        accountVO.setQrcode(accountDO.getQrcodeUrl());
        accountVO.setServiceType(accountDO.getServiceType());
        accountVO.setVerifyType(accountDO.getVerifyType());
        return new JsonResponse(accountVO);
    }

    /**
     * 解绑账户
     *
     * @param siteId 站点id
     * @return
     */
    @RequestMapping(value = "/account/unbind", method = RequestMethod.POST)
    public JsonResponse unbind(@Validate(key = "siteId") @RequestParam long siteId, @Validate(key = "postData", path = "json-schema/account/unbind-postdata-schema.json") @RequestBody String postData) throws ParamException, RedisException, DaoException, AccountNotExistException, JsonParseException {
        AccountDO account = accountService.getAccountBySiteId(siteId);
        JSONObject jsonObject = new JSONObject(postData);
        int type = jsonObject.getInt("type");
        String text = jsonObject.getString("text");
        UnbindDO unbind = new UnbindDO();
        unbind.setUnbindText(text);
        unbind.setUnbindType(type);
        accountService.unbindAccount(account, unbind);
        return new JsonResponse(null);
    }

    /**
     * 修改app_secret
     *
     * @return
     */
    @RequestMapping(value = "/account", method = RequestMethod.PUT)
    public JsonResponse modifyAppSecret(@RequestParam long siteId, @RequestBody String postData) throws ParamException, DaoException {
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

}
