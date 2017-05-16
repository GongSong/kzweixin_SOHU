package com.kuaizhan.controller;

import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.param.AddSysTplParam;
import com.kuaizhan.param.SendTplMsgParam;
import com.kuaizhan.pojo.po.AccountPO;
import com.kuaizhan.pojo.vo.JsonResponse;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.TplService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 模板消息controller
 * Created by zixiong on 2017/5/13.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION + "/tpl", produces = AppConstant.PRODUCES)
public class TplMsgController extends BaseController {
    @Resource
    private TplService tplService;
    @Resource
    private AccountService accountService;

    private static final Logger logger = Logger.getLogger(TplMsgController.class);

    @RequestMapping(value = "/sys_tpl_msgs", method = RequestMethod.POST)
    public JsonResponse sendSysTplMsgs(@Valid @RequestBody SendTplMsgParam param) {
        AccountPO accountPO = accountService.getAccountBySiteId(param.getSiteId());
        tplService.sendSysTplMsg(accountPO.getWeixinAppId(), param.getTemplateId(), param.getOpenId(),
                param.getUrl(), param.getData());
        return new JsonResponse(null);
    }

    @RequestMapping(value = "/sys_tpls", method = RequestMethod.POST)
    public JsonResponse addSysTpl(@Valid @RequestBody AddSysTplParam param) {
        AccountPO accountPO = accountService.getAccountBySiteId(param.getSiteId());
        tplService.addTpl(accountPO.getWeixinAppId(), param.getTemplateId());
        return new JsonResponse(null);
    }
}
