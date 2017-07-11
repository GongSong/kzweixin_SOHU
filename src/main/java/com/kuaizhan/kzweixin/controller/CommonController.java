/**
 * 公共模块
 */
package com.kuaizhan.kzweixin.controller;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.service.AccountService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 通用接口
 * Created by zixiong on 2017/4/16.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION + "/common", produces = "application/json")
public class CommonController extends BaseController{

    @Resource
    private AccountService accountService;

    /**
     * ping, 用于安全检查
     */
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public JsonResponse ping(){
        return new JsonResponse(ImmutableMap.of());
    }

    @RequestMapping(value = "/access_token", method = RequestMethod.GET)
    public JsonResponse getAccessToken(@RequestParam long weixinAppid){
        String accessToken = accountService.getAccessToken(weixinAppid);
        return new JsonResponse(ImmutableMap.of("token", accessToken));
    }
}
