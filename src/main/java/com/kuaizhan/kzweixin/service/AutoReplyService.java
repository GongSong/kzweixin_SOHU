package com.kuaizhan.kzweixin.service;

import com.kuaizhan.kzweixin.controller.param.KeywordParamItem;
import com.kuaizhan.kzweixin.dao.po.auto.FollowReplyPO;
import com.kuaizhan.kzweixin.dao.po.auto.KeywordReplyPO;
import com.kuaizhan.kzweixin.dao.po.auto.MsgReplyPO;
import com.kuaizhan.kzweixin.entity.responsejson.ResponseJson;
import com.kuaizhan.kzweixin.enums.ComponentResponseType;

import java.util.List;

/**
 * Created by fangtianyu on 7/31/17.
 */
public interface AutoReplyService {

    /**
     * 创建新规则
     * @param ruleName 规则名
     * @param keywords 关键词列表
     * @param responseType 自动回复消息种类
     * @param responseJson 自动回复消息内容
     * @return 规则Id
     * */
    long createRule(long weixinAppid, String ruleName, List<KeywordParamItem> keywords,
                   ComponentResponseType responseType, ResponseJson responseJson);

    /**
     * 获取规则列表
     * @param query 规则名及关键词匹配
     * @return 规则列表
     * */
    List<KeywordReplyPO> getRules(long weixinAppid, String query);

    /**
     * 更新规则
     * @param ruleId 更新的规则Id
     * @param ruleName 规则名
     * @param keywords 关键词列表
     * @param responseType 自动回复消息种类
     * @param responseJson 自动回复消息内容
     * */
    void updateRule(long ruleId, String ruleName, List<KeywordParamItem> keywords,
                    ComponentResponseType responseType, ResponseJson responseJson);

    /**
     * 删除规则
     * */
    void deleteRule(long ruleId);

    /**
     * 创建/更新被关注自动回复
     * */
    int createSubscribeReply(long weixinAppid, ComponentResponseType responseType,
                             ResponseJson responseJson);

    /**
     * 获取被关注自动回复内容
     * */
    FollowReplyPO getSubscribeReply(long weixinAppid);

    /**
     * 删除被关注自动回复
     * */
    void deleteSubscribeReply(int followReplyId);

    /**
     * 创建/更新消息自动回复
     * */
    int createMsgReply(long weixinAppid, ComponentResponseType responseType,
                        ResponseJson responseJson);

    /**
     * 获取消息自动回复内容
     * */
    MsgReplyPO getMsgReply(long weixinAppid);

    /**
     * 删除消息自动回复
     * */
    void deleteMsgReply(int msgReplyId);
}
