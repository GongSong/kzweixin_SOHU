package com.kuaizhan.kzweixin.controller;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.controller.param.KeywordReplyRuleParam;
import com.kuaizhan.kzweixin.controller.param.AutoReplySubscribeParam;
import com.kuaizhan.kzweixin.controller.param.KeywordParamItem;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.entity.responsejson.*;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.springframework.web.bind.annotation.*;
import com.kuaizhan.kzweixin.service.AutoReplyService;
import com.kuaizhan.kzweixin.service.CommonService;
import com.kuaizhan.kzweixin.dao.po.auto.KeywordReplyPO;
import com.kuaizhan.kzweixin.dao.po.auto.FollowReplyPO;
import com.kuaizhan.kzweixin.dao.po.auto.MsgReplyPO;
import com.kuaizhan.kzweixin.controller.vo.KeywordVO;
import com.kuaizhan.kzweixin.controller.vo.AutoReplyVO;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fangtianyu on 7/31/17.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION, produces = AppConstant.PRODUCES)
public class AutoReplyController extends BaseController {

    @Resource
    private AutoReplyService autoReplyService;
    @Resource
    private CommonService commonService;

    /**
     * 创建新规则
     */
    @RequestMapping(value = "/autoreply/keyword_replies", method = RequestMethod.POST)
    public JsonResponse createRule(@Valid @RequestBody KeywordReplyRuleParam param) {
        ResponseJson responseJson = commonService.getResponseJsonFromParam(param.getWeixinAppid(),
                param.getResponseJson(), param.getResponseType());
        long ruleId = autoReplyService.createRule(param.getWeixinAppid(), param.getRuleName(),
                param.getKeywords(), param.getResponseType(), responseJson);
        return new JsonResponse(ImmutableMap.of("ruleId", ruleId, "ruleName", param.getRuleName()));
    }

    /**
     * 获取规则列表
     * */
    @RequestMapping(value = "/autoreply/keyword_replies", method = RequestMethod.GET)
    public JsonResponse getRules(@RequestParam long weixinAppid, @RequestParam(required = false) String query) {
        List<KeywordReplyPO> keywordReplyPOList = autoReplyService.getRules(weixinAppid, query);
        List<KeywordVO> keywordVOList = new ArrayList<>();

        for (KeywordReplyPO keywordReplyPO: keywordReplyPOList) {
            KeywordVO keywordVO = new KeywordVO();
            keywordVO.setRuleId(keywordReplyPO.getRuleId());
            keywordVO.setWeixinAppid(keywordReplyPO.getWeixinAppid());
            keywordVO.setRuleName(keywordReplyPO.getRuleName());
            keywordVO.setResponseType(keywordReplyPO.getResponseType());

            Map<String, String> keywordsMap = JsonUtil.string2Bean(keywordReplyPO.getKeywordsJson(), Map.class);
            List<KeywordParamItem> keywords = new ArrayList<>();
            for (Map.Entry<String, String> curr: keywordsMap.entrySet()) {
                KeywordParamItem item = new KeywordParamItem();
                item.setKeyword(curr.getKey());
                item.setType(curr.getValue());
                keywords.add(item);
            }

            keywordVO.setKeywords(keywords);
            ResponseJson responseJson = commonService.getResponseJsonFromDB(keywordReplyPO.getResponseJson(), keywordReplyPO.getResponseType());
            keywordVO.setResponseJson(responseJson);
            keywordVOList.add(keywordVO);
        }

        return new JsonResponse(keywordVOList);
    }

    /**
     * 更新规则
     * */
    @RequestMapping(value = "/autoreply/keyword_replies/{ruleId}", method = RequestMethod.PUT)
    public JsonResponse updateRule(@PathVariable("ruleId") long ruleId, @Valid @RequestBody KeywordReplyRuleParam param) {
        ResponseJson responseJson = commonService.getResponseJsonFromParam(param.getWeixinAppid(),
                param.getResponseJson(), param.getResponseType());
        autoReplyService.updateRule(ruleId, param.getRuleName(), param.getKeywords(), param.getResponseType(), responseJson);
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 删除规则
     * */
    @RequestMapping(value = "/autoreply/keyword_replies/{ruleId}", method = RequestMethod.DELETE)
    public JsonResponse deleteRule(@PathVariable("ruleId") long ruleId, @RequestParam long weixinAppid) {
        autoReplyService.deleteRule(ruleId);
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 创建/更新被关注自动回复
     * */
    @RequestMapping(value = "/autoreply/follow_reply", method = RequestMethod.PUT)
    public JsonResponse createSubscribeReply(@Valid @RequestBody AutoReplySubscribeParam param) {
        ResponseJson responseJson = commonService.getResponseJsonFromParam(param.getWeixinAppid(),
                param.getResponseJson(), param.getResponseType());
        int followReplyId = autoReplyService.createSubscribeReply(param.getWeixinAppid(), param.getResponseType(), responseJson);
        return new JsonResponse(ImmutableMap.of("followReplyId", followReplyId));
    }

    /**
     * 获取被关注自动回复内容
     * */
    @RequestMapping(value = "/autoreply/follow_reply", method = RequestMethod.GET)
    public JsonResponse getSubscribeReply(@RequestParam long weixinAppid) {
        FollowReplyPO followReplyPO = autoReplyService.getSubscribeReply(weixinAppid);
        if (followReplyPO == null) {
            return new JsonResponse(ImmutableMap.of());
        }

        AutoReplyVO autoReplyVO = new AutoReplyVO();
        autoReplyVO.setId(followReplyPO.getId());
        autoReplyVO.setWeixinAppid(followReplyPO.getWeixinAppid());
        autoReplyVO.setResponseType(followReplyPO.getResponseType());
        ResponseJson responseJson = commonService.getResponseJsonFromDB(followReplyPO.getResponseJson(), followReplyPO.getResponseType());
        autoReplyVO.setResponseJson(responseJson);

        return new JsonResponse(autoReplyVO);
    }

    /**
     * 删除被关注自动回复
     * */
    @RequestMapping(value = "/autoreply/follow_reply/{followReplyId}", method = RequestMethod.DELETE)
    public JsonResponse deleteSubscribeReply(@PathVariable("followReplyId") int followReplyId, @RequestParam long weixinAppid) {
        autoReplyService.deleteSubscribeReply(followReplyId);
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 创建/更新消息自动回复
     * */
    @RequestMapping(value = "/autoreply/msg_reply", method = RequestMethod.PUT)
    public JsonResponse createMsgReply(@Valid @RequestBody AutoReplySubscribeParam param) {
        ResponseJson responseJson = commonService.getResponseJsonFromParam(param.getWeixinAppid(),
                param.getResponseJson(), param.getResponseType());
        int msgReplyId = autoReplyService.createMsgReply(param.getWeixinAppid(), param.getResponseType(), responseJson);
        return new JsonResponse(ImmutableMap.of("msgReplyId", msgReplyId));
    }

    /**
     * 获取消息自动回复内容
     * */
    @RequestMapping(value = "/autoreply/msg_reply", method = RequestMethod.GET)
    public JsonResponse getMsgReply(@RequestParam long weixinAppid) {
        MsgReplyPO msgReplyPO = autoReplyService.getMsgReply(weixinAppid);
        if (msgReplyPO == null) {
            return new JsonResponse(ImmutableMap.of());
        }

        AutoReplyVO autoReplyVO = new AutoReplyVO();
        autoReplyVO.setId(msgReplyPO.getId());
        autoReplyVO.setWeixinAppid(msgReplyPO.getWeixinAppid());
        autoReplyVO.setResponseType(msgReplyPO.getResponseType());
        ResponseJson responseJson = commonService.getResponseJsonFromDB(msgReplyPO.getResponseJson(), msgReplyPO.getResponseType());
        autoReplyVO.setResponseJson(responseJson);

        return new JsonResponse(autoReplyVO);
    }

    /**
     * 删除消息自动回复
     * */
    @RequestMapping(value = "/autoreply/msg_reply/{msgReplyId}", method = RequestMethod.DELETE)
    public JsonResponse deleteMsgReply(@PathVariable("msgReplyId") int msgReplyId, @RequestParam long weixinAppid) {
        autoReplyService.deleteMsgReply(msgReplyId);
        return new JsonResponse(ImmutableMap.of());
    }
}
