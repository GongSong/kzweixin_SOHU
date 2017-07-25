package com.kuaizhan.kzweixin.controller;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.controller.converter.ActionConverter;
import com.kuaizhan.kzweixin.controller.param.AddActionParam;
import com.kuaizhan.kzweixin.controller.param.UpdateActionParam;
import com.kuaizhan.kzweixin.controller.vo.ActionVO;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.dao.po.auto.ActionPO;
import com.kuaizhan.kzweixin.entity.action.NewsResponse;
import com.kuaizhan.kzweixin.entity.action.TextResponse;
import com.kuaizhan.kzweixin.enums.ActionType;
import com.kuaizhan.kzweixin.enums.ResponseType;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.ActionService;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by zixiong on 2017/6/26.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION, produces = AppConstant.PRODUCES)
public class ActionController extends BaseController {

    @Resource
    private ActionService actionService;

    @Resource
    private AccountService accountService;

    /**
     * 新增action
     */
    @RequestMapping(value = "/actions", method = RequestMethod.POST)
    public JsonResponse addAction(@Valid @RequestBody AddActionParam param) {
        if (param.getActionType() == ActionType.REPLY && param.getKeyword() == null) {
            return new JsonResponse(ErrorCode.PARAM_ERROR.getCode(),
                    "keyword can not be null",
                    ImmutableMap.of());
        }

        long weixinAppid = accountService.getWeixinAppidFromAccountId(param.getAccountId());
        ActionPO actionPO = new ActionPO();
        actionPO.setWeixinAppid(weixinAppid);
        actionPO.setBizCode(param.getBizCode().getValue());
        actionPO.setKeyword(param.getKeyword());
        actionPO.setActionType(param.getActionType().getValue());
        actionPO.setResponseType(param.getResponseType().getValue());
        actionPO.setStatus(true);

        Object responseObj = null;
        String responseJson = JsonUtil.bean2String(param.getResponseJson());
        if (param.getResponseType() == ResponseType.TEXT) {
            responseObj = JsonUtil.string2Bean(responseJson, TextResponse.class);
        } else if (param.getResponseType() == ResponseType.NEWS) {
            responseObj = JsonUtil.string2Bean(responseJson, NewsResponse.class);
        }

        int id = actionService.addAction(weixinAppid, actionPO, responseObj);
        return new JsonResponse(ImmutableMap.of("id", id));
    }

    /**
     * 获取action详情
     */
    @RequestMapping(value = "/actions/{actionId}", method = RequestMethod.GET)
    public JsonResponse getAction(@PathVariable int actionId) {
        ActionPO actionPO = actionService.getActionById(actionId);
        ActionVO actionVO = ActionConverter.toActionVo(actionPO);
        return new JsonResponse(actionVO);
    }

    /**
     * 修改action
     */
    @RequestMapping(value = "/actions/{actionId}", method = RequestMethod.PUT)
    public JsonResponse updateAction(@PathVariable int actionId, @Valid @RequestBody UpdateActionParam param) {

        ActionPO actionPO = new ActionPO();
        actionPO.setId(actionId);
        actionPO.setKeyword(param.getKeyword());
        if (param.getBizCode() != null) {
            actionPO.setBizCode(param.getBizCode().getValue());
        }
        if (param.getActionType() != null) {
            actionPO.setActionType(param.getActionType().getValue());
        }
        if (param.getResponseType() != null) {
            actionPO.setResponseType(param.getResponseType().getValue());
        }
        actionPO.setStatus(param.getStatus());

        if (param.getResponseJson() != null) {
            String responseJson = JsonUtil.bean2String(param.getResponseJson());
            actionPO.setResponseJson(responseJson);
            // 做下校验

            if (param.getResponseType() == ResponseType.TEXT) {
                JsonUtil.string2Bean(responseJson, TextResponse.class);
            } else if (param.getResponseType() == ResponseType.NEWS) {
                JsonUtil.string2Bean(responseJson, NewsResponse.class);
            }
        }
        actionService.updateAction(actionPO);
        return new JsonResponse(ImmutableMap.of());
    }
}
