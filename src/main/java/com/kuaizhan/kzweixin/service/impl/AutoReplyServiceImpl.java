package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.entity.autoreply.KeywordItem;
import com.kuaizhan.kzweixin.entity.responsejson.ResponseJson;
import com.kuaizhan.kzweixin.enums.ComponentResponseType;
import com.kuaizhan.kzweixin.service.AutoReplyService;
import com.kuaizhan.kzweixin.service.CommonService;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import com.kuaizhan.kzweixin.dao.po.auto.KeywordReplyPO;
import com.kuaizhan.kzweixin.dao.po.auto.FollowReplyPO;
import com.kuaizhan.kzweixin.dao.po.auto.MsgReplyPO;
import com.kuaizhan.kzweixin.dao.po.auto.KeywordReplyPOExample;
import com.kuaizhan.kzweixin.dao.po.auto.FollowReplyPOExample;
import com.kuaizhan.kzweixin.dao.po.auto.MsgReplyPOExample;
import com.kuaizhan.kzweixin.dao.mapper.auto.KeywordReplyMapper;
import com.kuaizhan.kzweixin.dao.mapper.auto.FollowReplyMapper;
import com.kuaizhan.kzweixin.dao.mapper.auto.MsgReplyMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fangtianyu on 7/31/17.
 */
@Service
public class AutoReplyServiceImpl implements AutoReplyService {

    @Resource
    private KeywordReplyMapper keywordReplyMapper;
    @Resource
    private FollowReplyMapper followReplyMapper;
    @Resource
    private MsgReplyMapper msgReplyMapper;
    @Resource
    private CommonService commonService;

    @Override
    public long createKeywordRule(long weixinAppid, String ruleName, List<KeywordItem> keywords,
                           ComponentResponseType responseType, ResponseJson responseJson) {
        String keywordStr = convertBeforeInsert(keywords);

        KeywordReplyPO record = new KeywordReplyPO();
        record.setWeixinAppid(weixinAppid);
        record.setRuleName(ruleName);
        record.setKeywordsJson(keywordStr);
        record.setResponseType(responseType);

        responseJson.cleanBeforeInsert();
        record.setResponseJson(JsonUtil.bean2String(responseJson));
        record.setStatus(1);
        record.setCreateTime(DateUtil.curSeconds());
        record.setUpdateTime(DateUtil.curSeconds());

        keywordReplyMapper.insert(record);
        return record.getRuleId();
    }

    @Override
    public List<KeywordReplyPO> getKeywordRules(long weixinAppid, String query) {
        KeywordReplyPOExample example = new KeywordReplyPOExample();
        KeywordReplyPOExample.Criteria criteria = example.createCriteria();

        criteria.andWeixinAppidEqualTo(weixinAppid)
                .andStatusEqualTo(1);
        example.setOrderByClause("update_time");

        if (query != null && !"".equals(query)) {
            criteria.andRuleNameLike(query)
                    .andKeywordsJsonLike(query);
        }
        List<KeywordReplyPO> keywordReplyPOList = keywordReplyMapper.selectByExampleWithBLOBs(example);
        Collections.reverse(keywordReplyPOList);

        return keywordReplyPOList;
    }

    @Override
    public void updateKeywordRule(long ruleId, String ruleName, List<KeywordItem> keywords,
                           ComponentResponseType responseType, ResponseJson responseJson) {
        String keywordStr = convertBeforeInsert(keywords);

        KeywordReplyPO record = new KeywordReplyPO();
        record.setRuleId(ruleId);
        record.setRuleName(ruleName);
        record.setKeywordsJson(keywordStr);
        record.setResponseType(responseType);

        responseJson.cleanBeforeInsert();
        record.setResponseJson(JsonUtil.bean2String(responseJson));
        record.setUpdateTime(DateUtil.curSeconds());

        keywordReplyMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void deleteKeywordRule(long ruleId) {
        KeywordReplyPO record = new KeywordReplyPO();
        record.setRuleId(ruleId);
        record.setStatus(2);
        keywordReplyMapper.updateByPrimaryKeySelective(record);
    }


    private String convertBeforeInsert(List<KeywordItem> keywords) {
        Map<String, String> keywordsMap = new HashMap<>();
        for (KeywordItem curr : keywords) {
            keywordsMap.put(curr.getKeyword(), curr.getType());
        }
        return JsonUtil.bean2String(keywordsMap);
    }

    @Override
    public int createFollowReply(long weixinAppid, ComponentResponseType responseType,
                                    ResponseJson responseJson) {
        FollowReplyPO followReplyPO = getFollowReply(weixinAppid);

        FollowReplyPO record = new FollowReplyPO();
        record.setResponseType(responseType);
        responseJson.cleanBeforeInsert();
        record.setResponseJson(JsonUtil.bean2String(responseJson));
        record.setUpdateTime(DateUtil.curSeconds());

        if (followReplyPO == null) {
            record.setWeixinAppid(weixinAppid);
            record.setStatus(0);
            record.setCreateTime(DateUtil.curSeconds());
            followReplyMapper.insert(record);
        } else {
            record.setId(followReplyPO.getId());
            followReplyMapper.updateByPrimaryKeySelective(record);
        }

        return record.getId();

    }

    @Override
    public FollowReplyPO getFollowReply(long weixinAppid) {
        FollowReplyPOExample example = new FollowReplyPOExample();
        example.createCriteria()
                .andWeixinAppidEqualTo(weixinAppid)
                .andStatusEqualTo(0);
        example.setOrderByClause("update_time");
        List<FollowReplyPO> followPOList =  followReplyMapper.selectByExampleWithBLOBs(example);
        return followPOList == null || followPOList.size() == 0 ? null : followPOList.get(0);
    }

    @Override
    public void deleteFollowReply(long weixinAppid) {
        FollowReplyPOExample example = new FollowReplyPOExample();
        example.createCriteria()
                .andWeixinAppidEqualTo(weixinAppid);

        FollowReplyPO record = new FollowReplyPO();
        record.setStatus(1);

        followReplyMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int createMsgReply(long weixinAppid, ComponentResponseType responseType,
                              ResponseJson responseJson) {
        MsgReplyPO msgReplyPO = getMsgReply(weixinAppid);

        MsgReplyPO record = new MsgReplyPO();
        record.setResponseType(responseType);
        responseJson.cleanBeforeInsert();
        record.setResponseJson(JsonUtil.bean2String(responseJson));
        record.setUpdateTime(DateUtil.curSeconds());

        if (msgReplyPO == null) {
            record.setWeixinAppid(weixinAppid);
            record.setStatus(0);
            record.setCreateTime(DateUtil.curSeconds());
            msgReplyMapper.insert(record);
        } else {
            record.setId(msgReplyPO.getId());
            msgReplyMapper.updateByPrimaryKeySelective(record);
        }

        return record.getId();
    }

    @Override
    public MsgReplyPO getMsgReply(long weixinAppid) {
        MsgReplyPOExample example = new MsgReplyPOExample();
        example.createCriteria()
                .andWeixinAppidEqualTo(weixinAppid)
                .andStatusEqualTo(0);
        example.setOrderByClause("update_time");
        List<MsgReplyPO> msgReplyPOList = msgReplyMapper.selectByExampleWithBLOBs(example);
        return msgReplyPOList == null || msgReplyPOList.size() == 0 ? null : msgReplyPOList.get(0);
    }

    @Override
    public void deleteMsgReply(long weixinAppid) {
        MsgReplyPOExample example = new MsgReplyPOExample();
        example.createCriteria()
                .andWeixinAppidEqualTo(weixinAppid);

        MsgReplyPO record = new MsgReplyPO();
        record.setStatus(1);

        msgReplyMapper.updateByExampleSelective(record, example);
    }

    public ResponseJson getKeywordReplyService(String keyword, long weixinAppid) {
        KeywordReplyPOExample example = new KeywordReplyPOExample();
        example.createCriteria()
                .andWeixinAppidEqualTo(weixinAppid)
                .andStatusEqualTo(1);
        example.setOrderByClause("update_time");

        List<KeywordReplyPO> keywordReplyPOList = keywordReplyMapper.selectByExampleWithBLOBs(example);

        for (int i = keywordReplyPOList.size() - 1; i >= 0; i--) {
            KeywordReplyPO currPO = keywordReplyPOList.get(i);
            Map<String, String> keywordMap = JsonUtil.string2Bean(currPO.getKeywordsJson(), Map.class);
            if (keywordMap.containsKey(keyword)) {
                return commonService.getResponseJsonFromDB(currPO.getResponseJson(), currPO.getResponseType());
            }
            for (Map.Entry<String, String> curr: keywordMap.entrySet()) {
                if ("0".equals(curr.getValue()) && keyword.contains(curr.getKey())) {
                    return commonService.getResponseJsonFromDB(currPO.getResponseJson(), currPO.getResponseType());
                }
            }
        }

        return null;
    }

    public ResponseJson getFollowReplyService(long weixinAppid) {
        FollowReplyPOExample example = new FollowReplyPOExample();
        example.createCriteria()
                .andWeixinAppidEqualTo(weixinAppid)
                .andStatusEqualTo(0);

        List<FollowReplyPO> followReplyPOList = followReplyMapper.selectByExampleWithBLOBs(example);

        if (followReplyPOList.size() == 0) {
            return null;
        }
        FollowReplyPO followReplyPO = followReplyPOList.get(0);
        return commonService.getResponseJsonFromDB(followReplyPO.getResponseJson(), followReplyPO.getResponseType());
    }

    public ResponseJson getMsgReplyService(long weixinAppid) {
        MsgReplyPOExample example = new MsgReplyPOExample();
        example.createCriteria()
                .andWeixinAppidEqualTo(weixinAppid)
                .andStatusEqualTo(0);

        List<MsgReplyPO> msgReplyPOList = msgReplyMapper.selectByExampleWithBLOBs(example);

        if (msgReplyPOList.size() == 0) {
            return null;
        }
        MsgReplyPO msgReplyPO = msgReplyPOList.get(0);
        return commonService.getResponseJsonFromDB(msgReplyPO.getResponseJson(), msgReplyPO.getResponseType());
    }

}
