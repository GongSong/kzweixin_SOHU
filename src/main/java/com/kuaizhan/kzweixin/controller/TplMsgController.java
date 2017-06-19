package com.kuaizhan.kzweixin.controller;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.controller.param.AddSysTplParam;
import com.kuaizhan.kzweixin.controller.param.SendTplMsgParam;
import com.kuaizhan.kzweixin.dao.po.auto.AccountPO;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.TplService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 模板消息模块接口
 * Created by zixiong on 2017/5/13.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION + "/tpl", produces = AppConstant.PRODUCES)
public class TplMsgController extends BaseController {
    @Resource
    private TplService tplService;
    @Resource
    private AccountService accountService;

    /**
     * 发送系统模板消息
     */
    @RequestMapping(value = "/sys_tpl_msgs", method = RequestMethod.POST)
    public JsonResponse sendSysTplMsgs(@Valid @RequestBody SendTplMsgParam param) {
        AccountPO accountPO = accountService.getAccountByAppId(param.getAppId());
        tplService.sendSysTplMsg(accountPO.getWeixinAppid(), param.getTemplateId(), param.getOpenId(),
                param.getUrl(), param.getData());
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 新增系统模板
     */
    @RequestMapping(value = "/sys_tpls", method = RequestMethod.POST)
    public JsonResponse addSysTpl(@Valid @RequestBody AddSysTplParam param) {
        AccountPO accountPO = accountService.getAccountBySiteId(param.getSiteId());
        tplService.addTpl(accountPO.getWeixinAppid(), param.getTemplateId());
        return new JsonResponse(ImmutableMap.of());
    }
}
