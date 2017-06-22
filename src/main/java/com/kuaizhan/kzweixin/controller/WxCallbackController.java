package com.kuaizhan.kzweixin.controller;


import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.service.ThirdPartService;
import com.kuaizhan.kzweixin.service.WeixinMsgService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 微信回调接口
 * Created by Mr.Jadyn on 2017/1/11.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION)
public class WxCallbackController extends BaseController {

    @Resource
    private ThirdPartService thirdPartService;
    @Resource
    private WeixinMsgService weixinMsgService;

    /**
     * 获取微信推送的component_verify_ticket
     */
    @RequestMapping(value = "/auth/tickets", method = RequestMethod.POST)
    public String receiver(@RequestParam("msg_signature") String signature,
                           @RequestParam String timestamp,
                           @RequestParam String nonce,
                           @RequestBody String postData) {
        thirdPartService.refreshComponentVerifyTicket(signature, timestamp, nonce, postData);
        return "success";
    }


    /**
     * 微信消息推送
     */
    @RequestMapping(value = "/accounts/{appId}/events", method = RequestMethod.POST, produces = "application/xml;charset=UTF-8")
    public String weixinPush(@PathVariable String appId,
                             @RequestParam("msg_signature") String signature,
                             @RequestParam String timestamp,
                             @RequestParam String nonce,
                             @RequestBody(required = false) String postData) {
        //检查消息是否来自微信
//        return weixinMsgService.handleWeixinPushMsg(appId, signature, timestamp, nonce, postData);
        return "success";
    }
}
