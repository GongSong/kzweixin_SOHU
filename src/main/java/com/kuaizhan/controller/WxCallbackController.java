package com.kuaizhan.controller;


import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.exception.common.DaoException;
import com.kuaizhan.exception.common.RedisException;
import com.kuaizhan.exception.common.XMLParseException;
import com.kuaizhan.exception.deprecated.system.*;
import com.kuaizhan.service.WeixinAuthService;
import com.kuaizhan.service.WeixinMsgService;
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
}
