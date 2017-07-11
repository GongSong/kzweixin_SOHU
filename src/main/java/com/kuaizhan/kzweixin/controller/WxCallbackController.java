package com.kuaizhan.kzweixin.controller;


import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.WxPushService;
import com.kuaizhan.kzweixin.service.WxThirdPartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;


/**
 * 微信回调接口
 * Created by Mr.Jadyn on 2017/1/11.
 */
@RestController
@RequestMapping(value = "public/" + AppConstant.VERSION)
public class WxCallbackController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(WxCallbackController.class);

    @Resource
    private WxThirdPartService wxThirdPartService;
    @Resource
    private AccountService accountService;
    @Resource
    private WxPushService wxPushService;

    // 全网发布测试appid
    private static final String wxTestAppid = "wx570bc396a51b8ff8";

    private static final String SUCCESS_RESULT = "success";

    /**
     * 新增绑定，微信服务器跳转回来
     */
    @RequestMapping(value = "/bind_redirect", method = RequestMethod.GET)
    public RedirectView addBindAccount(@RequestParam Long userId,
                                       @RequestParam(required = false) Long siteId,
                                       @RequestParam String redirectUrl,
                                       @RequestParam(value = "auth_code") String authCode) {
        accountService.bindAccount(userId, authCode, siteId);
        return new RedirectView(redirectUrl);
    }

    /**
     * 获取微信推送的component_verify_ticket
     */
    @RequestMapping(value = "/auth/tickets")
    public String refreshTicket(@RequestParam("msg_signature") String signature,
                                @RequestParam String timestamp,
                                @RequestParam String nonce,
                                @RequestBody String postData) {
        String xmlStr = wxThirdPartService.decryptMsg(signature, timestamp, nonce, postData);
        wxThirdPartService.refreshComponentVerifyTicket(xmlStr);
        return "success";
    }


    /**
     * 微信消息推送
     */
    @RequestMapping(value = "/accounts/{appId}/events")
    public String handleEventPush(@PathVariable String appId,
                                  @RequestParam(required = false, value = "msg_signature") String signature,
                                  @RequestParam(required = false) String timestamp,
                                  @RequestParam(required = false) String nonce,
                                  @RequestBody(required = false) String postData) {

        // 参数发现过非微信服务器ip, 用不规范的参数请求。过滤掉非法参数
        if (signature == null || timestamp == null || nonce == null || postData == null) {
            return SUCCESS_RESULT;
        }

        String xmlStr = wxThirdPartService.decryptMsg(signature, timestamp, nonce, postData);

        // 判断是否是全网发布测试的appid
        if (wxTestAppid.equals(appId)) {
            return wxPushService.handleTestEventPush(timestamp, nonce, xmlStr);
        }

        // 记录开始时间
        long startTime = System.currentTimeMillis();

        String resultStr = SUCCESS_RESULT;
        try {
            resultStr = wxPushService.handleEventPush(appId, signature, timestamp, nonce, xmlStr);
        } catch (Exception e) {
            // 抛异常，返回success，记录错误日志
            logger.error("[WxCallback] 回调处理失败, appid: {}, xmlStr: {}", appId, xmlStr, e);
        }

        long delta = System.currentTimeMillis() - startTime;
        if (delta > 5 * 1000) {
            // 超过5秒error日志
            logger.error("[Weixin:event] handle time up to 5 seconds, time: {}, xmlStr: {}", delta, xmlStr);
        } else if (delta > 3 * 1000) {
            // 超过3秒warning日志
            logger.warn("[Weixin:event] handle time up to 3 seconds, time: {}, xmlStr: {}", delta, xmlStr);
        }

        return resultStr;
    }
}
