package com.kuaizhan.controller;


import com.kuaizhan.config.ApplicationConfig;
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
import org.apache.ibatis.annotations.Param;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账号controller
 * Created by Mr.Jadyn on 2017/1/25.
 */
@RestController
@RequestMapping(value = ApplicationConfig.VERSION, produces = "application/json")
public class AccountController extends BaseController {
    @Resource
    AccountService accountService;

    /**
     * 获取账户信息
     *
     * @param siteId 站点id
     * @return
     */
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public JsonResponse getAccountInfo(@RequestParam long siteId) throws RedisException, DaoException, AccountNotExistException, IOException, JsonParseException {

        AccountDO accountDO = accountService.getAccountBySiteId(siteId);

        AccountVO accountVO = new AccountVO();
        accountVO.setAppId(accountDO.getWeixinAppId());
        accountVO.setAppSecret(accountDO.getAppSecret());
        accountVO.setHeadImg(accountDO.getHeadImg());
        List<String> list = JsonUtil.string2List(accountDO.getInterestJson(), String.class);
        accountVO.setInterest(list);
        accountVO.setName(accountDO.getNickName());
        accountVO.setQrcode(accountDO.getQrcodeUrl());
        accountVO.setType(accountDO.getServiceType());

        return new JsonResponse(accountVO);
    }

    /**
     * 解绑账户
     *
     * @param siteId 站点id
     * @return
     */
    @RequestMapping(value = "/account/unbind", method = RequestMethod.POST)
    public JsonResponse unbind(@RequestParam long siteId, @RequestBody String postData) throws ParamException, RedisException, DaoException, AccountNotExistException, JsonParseException {
        int type;
        String text;
        AccountDO account = accountService.getAccountBySiteId(siteId);

        try {
            JSONObject jsonObject = new JSONObject(postData);
            type = jsonObject.getInt("type");
            text = jsonObject.getString("text");
        } catch (Exception e) {
            throw new ParamException();
        }

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
        accountService.updateAppSecrect(siteId, appSecret);
        return new JsonResponse(null);
    }

}
