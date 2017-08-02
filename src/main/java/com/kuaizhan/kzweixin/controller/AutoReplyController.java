package com.kuaizhan.kzweixin.controller;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.controller.param.AutoReplyRuleParam;
import com.kuaizhan.kzweixin.controller.param.KeywordParamItem;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.entity.responsejson.*;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.springframework.web.bind.annotation.*;
import com.kuaizhan.kzweixin.service.AutoReplyService;
import com.kuaizhan.kzweixin.service.CommonService;
import com.kuaizhan.kzweixin.dao.po.auto.KeywordPO;
import com.kuaizhan.kzweixin.controller.vo.KeywordVO;

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
    public JsonResponse createRule(@Valid @RequestBody AutoReplyRuleParam param) {
        ResponseJson responseJson = commonService.getResponseJsonFromParam(param.getWeixinAppid(),
                param.getResponseJson(), param.getResponseType());
        int ruleId = autoReplyService.createRule(param.getWeixinAppid(), param.getRuleName(),
                param.getKeywords(), param.getResponseType(), responseJson);
        return new JsonResponse(ImmutableMap.of("id", ruleId, "name", param.getRuleName()));
    }

    /**
     * 获取规则列表
     * */
    @RequestMapping(value = "/autoreply/keyword_replies", method = RequestMethod.GET)
    public JsonResponse getRules(@RequestParam long weixinAppid, @RequestParam(required = false) String query) {
        List<KeywordPO> keywordPOList = autoReplyService.getRules(weixinAppid, query);
        List<KeywordVO> keywordVOList = new ArrayList<>();

        for (KeywordPO keywordPO: keywordPOList) {
            KeywordVO keywordVO = new KeywordVO();
            keywordVO.setRuleId(keywordPO.getRuleId());
            keywordVO.setWeixinAppid(keywordPO.getWeixinAppid());
            keywordVO.setRuleName(keywordPO.getRuleName());
            keywordVO.setResponseType(keywordPO.getResponseType());

            Map<String, String> keywordsMap = JsonUtil.string2Bean(keywordPO.getKeywordsJson(), Map.class);
            List<KeywordParamItem> keywords = new ArrayList<>();
            for (Map.Entry<String, String> curr: keywordsMap.entrySet()) {
                KeywordParamItem item = new KeywordParamItem();
                item.setKeyword(curr.getKey());
                item.setType(curr.getValue());
                keywords.add(item);
            }

            keywordVO.setKeywords(keywords);
            ResponseJson responseJson = commonService.getResponseJsonFromDB(keywordPO.getResponseJson(), keywordPO.getResponseType());
            keywordVO.setResponseJson(responseJson);
            keywordVOList.add(keywordVO);
        }

        return new JsonResponse(keywordVOList);
    }

    /**
     * 更新规则
     * */
    @RequestMapping(value = "/autoreply/keyword_replies/{ruleId}", method = RequestMethod.PUT)
    public JsonResponse updateRule(@PathVariable("ruleId") long ruleId, @Valid @RequestBody AutoReplyRuleParam param) {
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
























}
