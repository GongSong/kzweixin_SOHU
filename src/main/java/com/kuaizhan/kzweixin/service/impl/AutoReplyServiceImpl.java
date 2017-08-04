package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.controller.param.KeywordParamItem;
import com.kuaizhan.kzweixin.entity.responsejson.ResponseJson;
import com.kuaizhan.kzweixin.enums.ComponentResponseType;
import com.kuaizhan.kzweixin.service.AutoReplyService;
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

    @Override
    public long createRule(long weixinAppid, String ruleName, List<KeywordParamItem> keywords,
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
    public List<KeywordReplyPO> getRules(long weixinAppid, String query) {
        KeywordReplyPOExample example = new KeywordReplyPOExample();

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

        return keywordReplyMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public void updateRule(long ruleId, String ruleName, List<KeywordParamItem> keywords,
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
    public void deleteRule(long ruleId) {
        KeywordReplyPO record = new KeywordReplyPO();
        record.setRuleId(ruleId);
        record.setStatus(2);
        keywordReplyMapper.updateByPrimaryKeySelective(record);
    }


    private String convertBeforeInsert(List<KeywordParamItem> keywords) {
        Map<String, String> keywordsMap = new HashMap<>();
        for (KeywordParamItem curr : keywords) {
            keywordsMap.put(curr.getKeyword(), curr.getType());
        }
        return JsonUtil.bean2String(keywordsMap);
    }

    @Override
    public int createSubscribeReply(long weixinAppid, ComponentResponseType responseType,
                                    ResponseJson responseJson) {
        List<FollowReplyPO> followReplyPOList = getFollowPOByWeixinAppid(weixinAppid);

        FollowReplyPO record = new FollowReplyPO();
        record.setResponseType(responseType);
        responseJson.cleanBeforeInsert();
        record.setResponseJson(JsonUtil.bean2String(responseJson));
        record.setUpdateTime(DateUtil.curSeconds());

        if (followReplyPOList == null || followReplyPOList.size() == 0) {
            record.setWeixinAppid(weixinAppid);
            record.setStatus(0);
            record.setCreateTime(DateUtil.curSeconds());
            followReplyMapper.insert(record);
        } else {
            record.setId(followReplyPOList.get(0).getId());
            followReplyMapper.updateByPrimaryKeySelective(record);
        }

        return record.getId();

    }

    @Override
    public FollowReplyPO getSubscribeReply(long weixinAppid) {
        List<FollowReplyPO> followPOList = getFollowPOByWeixinAppid(weixinAppid);
        return followPOList == null || followPOList.size() == 0 ? null : followPOList.get(0);
    }

    @Override
    public void deleteSubscribeReply(int followReplyId) {
        FollowReplyPO record = new FollowReplyPO();
        record.setId(followReplyId);
        record.setStatus(1);
        followReplyMapper.updateByPrimaryKeySelective(record);
    }

    private List<FollowReplyPO> getFollowPOByWeixinAppid(long weixinAppid) {
        FollowReplyPOExample example = new FollowReplyPOExample();
        example.createCriteria()
                .andWeixinAppidEqualTo(weixinAppid)
                .andStatusEqualTo(0);
        example.setOrderByClause("update_time");
        return followReplyMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public int createMsgReply(long weixinAppid, ComponentResponseType responseType,
                              ResponseJson responseJson) {
        List<MsgReplyPO> msgReplyPOList = getMsgPOByWeixinAppid(weixinAppid);

        MsgReplyPO record = new MsgReplyPO();
        record.setResponseType(responseType);
        responseJson.cleanBeforeInsert();
        record.setResponseJson(JsonUtil.bean2String(responseJson));
        record.setUpdateTime(DateUtil.curSeconds());

        if (msgReplyPOList == null || msgReplyPOList.size() == 0) {
            record.setWeixinAppid(weixinAppid);
            record.setStatus(0);
            record.setCreateTime(DateUtil.curSeconds());
            msgReplyMapper.insert(record);
        } else {
            record.setId(msgReplyPOList.get(0).getId());
            msgReplyMapper.updateByPrimaryKeySelective(record);
        }

        return record.getId();
    }

    @Override
    public MsgReplyPO getMsgReply(long weixinAppid) {
        List<MsgReplyPO> msgReplyPOList = getMsgPOByWeixinAppid(weixinAppid);
        return msgReplyPOList == null || msgReplyPOList.size() == 0 ? null : msgReplyPOList.get(0);
    }

    @Override
    public void deleteMsgReply(int msgReplyId) {
        MsgReplyPO record = new MsgReplyPO();
        record.setId(msgReplyId);
        record.setStatus(1);
        msgReplyMapper.updateByPrimaryKeySelective(record);
    }

    private List<MsgReplyPO> getMsgPOByWeixinAppid(long weixinAppid) {
        MsgReplyPOExample example = new MsgReplyPOExample();
        example.createCriteria()
                .andWeixinAppidEqualTo(weixinAppid)
                .andStatusEqualTo(0);
        example.setOrderByClause("update_time");
        return msgReplyMapper.selectByExampleWithBLOBs(example);
    }


}
