package com.kuaizhan.kzweixin.controller;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.controller.converter.ActionConverter;
import com.kuaizhan.kzweixin.controller.param.AddActionParam;
import com.kuaizhan.kzweixin.controller.param.UpdateActionParam;
import com.kuaizhan.kzweixin.controller.vo.ActionVO;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.dao.po.auto.ActionPO;
import com.kuaizhan.kzweixin.entity.action.ActionResponse;
import com.kuaizhan.kzweixin.entity.action.NewsResponse;
import com.kuaizhan.kzweixin.entity.action.TextResponse;
import com.kuaizhan.kzweixin.enums.ActionType;
import com.kuaizhan.kzweixin.enums.ResponseType;
import com.kuaizhan.kzweixin.exception.common.ParamException;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.ActionService;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

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
            throw new ParamException("keyword can not be null");
        }

        long weixinAppid = accountService.getWeixinAppidFromAccountId(param.getAccountId());
        ActionPO actionPO = new ActionPO();
        actionPO.setWeixinAppid(weixinAppid);
        actionPO.setBizCode(param.getBizCode());
        actionPO.setKeyword(param.getKeyword());
        actionPO.setActionType(param.getActionType());
        actionPO.setResponseType(param.getResponseType());
        actionPO.setStatus(true);

        ActionResponse actionResponse = getActionResponse(param.getResponseType(),
                JsonUtil.bean2String(param.getResponseJson()));
        int id = actionService.addAction(weixinAppid, actionPO, actionResponse);

        return new JsonResponse(ImmutableMap.of("id", id));
    }

    private ActionResponse getActionResponse(ResponseType responseType, String responseJson) {
        if (responseType == ResponseType.TEXT) {
            return JsonUtil.string2Bean(responseJson, TextResponse.class);
        } else if (responseType == ResponseType.NEWS) {
            return JsonUtil.string2Bean(responseJson, NewsResponse.class);
        }
        return null;
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
        actionPO.setBizCode(param.getBizCode());
        actionPO.setActionType(param.getActionType());
        actionPO.setResponseType(param.getResponseType());
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

    /**
     * 根据token换取缓存的openId
     */
    @RequestMapping(value = "/action/open_id", method = RequestMethod.GET)
    public JsonResponse getOpenIdByToken(@RequestParam String token) {
        String openId = actionService.getOpenIdByToken(token);

        Map<String, String> result = new HashMap<>();
        result.put("openId", openId);
        return new JsonResponse(result);
    }
}
