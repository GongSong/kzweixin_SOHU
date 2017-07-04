package com.kuaizhan.kzweixin.controller;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.controller.param.AddActionParam;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.dao.po.auto.ActionPO;
import com.kuaizhan.kzweixin.entity.action.NewsResponse;
import com.kuaizhan.kzweixin.entity.action.TextResponse;
import com.kuaizhan.kzweixin.enums.ActionType;
import com.kuaizhan.kzweixin.enums.ResponseType;
import com.kuaizhan.kzweixin.service.ActionService;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/actions", method = RequestMethod.POST)
    public JsonResponse addActions(@Valid @RequestBody AddActionParam param) {
        if (param.getActionType() == ActionType.REPLY && param.getKeyword() == null) {
            return new JsonResponse(ErrorCode.PARAM_ERROR.getCode(),
                    "keyword can not be null",
                    ImmutableMap.of());
        }

        ActionPO actionPO = param.toActionPo();

        Object responseObj = null;
        String responseJson = JsonUtil.bean2String(param.getResponseJson());
        if (param.getResponseType() == ResponseType.TEXT) {
            responseObj = JsonUtil.string2Bean(responseJson, TextResponse.class);
        } else if (param.getResponseType() == ResponseType.NEWS) {
            responseObj = JsonUtil.string2Bean(responseJson, NewsResponse.class);
        }

        int id = actionService.addAction(param.getWeixinAppid(), actionPO, responseObj);
        return new JsonResponse(ImmutableMap.of("id", id));
    }
}
