package com.kuaizhan.controller;


import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.pojo.VO.JsonResponse;
import com.kuaizhan.service.AccountService;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 账号controller
 * Created by Mr.Jadyn on 2017/1/25.
 */
@RestController
@RequestMapping(value = ApplicationConfig.VERSION, produces = "application/json")
public class AccountController extends BaseController{
    @Resource
    AccountService accountService;

    /**
     * 获取账户信息
     *
     * @param siteId      站点id
     * @return
     */
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public JsonResponse getAccountInfo(@RequestParam long siteId) throws RedisException, DaoException {
        return new JsonResponse(accountService.getAccountBySiteId(siteId));
    }

}
