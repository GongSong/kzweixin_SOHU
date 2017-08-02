package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.controller.param.KeywordParamItem;
import com.kuaizhan.kzweixin.entity.responsejson.ResponseJson;
import com.kuaizhan.kzweixin.enums.ComponentResponseType;
import com.kuaizhan.kzweixin.service.AutoReplyService;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import com.kuaizhan.kzweixin.dao.po.auto.KeywordPO;
import com.kuaizhan.kzweixin.dao.po.auto.KeywordPOExample;
import com.kuaizhan.kzweixin.dao.mapper.auto.KeywordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fangtianyu on 7/31/17.
 */
@Service
public class AutoReplyServiceImpl implements AutoReplyService{

    @Resource
    private KeywordMapper keywordMapper;

    @Override
    public int createRule(long weixinAppid, String ruleName, List<KeywordParamItem> keywords,
                          ComponentResponseType responseType, ResponseJson responseJson) {
        String keywordStr = convertBeforeInsert(keywords);

        KeywordPO record = new KeywordPO();
        record.setWeixinAppid(weixinAppid);
        record.setRuleName(ruleName);
        record.setKeywordsJson(keywordStr);
        record.setResponseType(responseType);

        responseJson.cleanBeforeInsert();
        record.setResponseJson(JsonUtil.bean2String(responseJson));
        record.setStatus(1);
        record.setCreateTime(DateUtil.curSeconds());
        record.setUpdateTime(DateUtil.curSeconds());

        return keywordMapper.insert(record);
    }

    @Override
    public List<KeywordPO> getRules(long weixinAppid, String query) {
        KeywordPOExample example = new KeywordPOExample();

        if (query == null || "".equals(query)) {
            example.createCriteria()
                    .andWeixinAppidEqualTo(weixinAppid)
                    .andStatusEqualTo(1);
        } else {
            example.createCriteria()
                    .andWeixinAppidEqualTo(weixinAppid)
                    .andStatusEqualTo(1)
                    .andRuleNameLike(query)
                    .andKeywordsJsonLike(query);
        }

        return keywordMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public void updateRule(long ruleId, String ruleName, List<KeywordParamItem> keywords,
                           ComponentResponseType responseType, ResponseJson responseJson) {
        String keywordStr = convertBeforeInsert(keywords);

        KeywordPO record = new KeywordPO();
        record.setRuleId(ruleId);
        record.setRuleName(ruleName);
        record.setKeywordsJson(keywordStr);
        record.setResponseType(responseType);

        responseJson.cleanBeforeInsert();
        record.setResponseJson(JsonUtil.bean2String(responseJson));
        record.setUpdateTime(DateUtil.curSeconds());

        keywordMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void deleteRule(long ruleId) {
        KeywordPO record = new KeywordPO();
        record.setRuleId(ruleId);
        record.setStatus(2);
        keywordMapper.updateByPrimaryKeySelective(record);
    }


    private String convertBeforeInsert(List<KeywordParamItem> keywords) {
        Map<String, String> keywordsMap = new HashMap<>();
        for (KeywordParamItem curr: keywords) {
            keywordsMap.put(curr.getKeyword(), curr.getType());
        }
        return JsonUtil.bean2String(keywordsMap);
    }





}
