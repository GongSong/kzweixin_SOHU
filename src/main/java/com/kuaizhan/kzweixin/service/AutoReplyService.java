package com.kuaizhan.kzweixin.service;

import com.kuaizhan.kzweixin.entity.autoreply.KeywordItem;
import com.kuaizhan.kzweixin.dao.po.auto.FollowReplyPO;
import com.kuaizhan.kzweixin.dao.po.auto.KeywordReplyPO;
import com.kuaizhan.kzweixin.dao.po.auto.MsgReplyPO;
import com.kuaizhan.kzweixin.entity.responsejson.ResponseJson;
import com.kuaizhan.kzweixin.enums.ResponseType;

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
    long createKeywordRule(long weixinAppid, String ruleName, List<KeywordItem> keywords,
                           ResponseType responseType, ResponseJson responseJson);

    /**
     * 获取规则列表
     * @param query 规则名及关键词匹配
     * @return 规则列表
     * */
    List<KeywordReplyPO> getKeywordRules(long weixinAppid, String query);

    /**
     * 更新规则
     * @param ruleId 更新的规则Id
     * @param ruleName 规则名
     * @param keywords 关键词列表
     * @param responseType 自动回复消息种类
     * @param responseJson 自动回复消息内容
     * */
    void updateKeywordRule(long ruleId, String ruleName, List<KeywordItem> keywords,
                           ResponseType responseType, ResponseJson responseJson);

    /**
     * 删除规则
     * */
    void deleteKeywordRule(long ruleId);

    /**
     * 创建/更新被关注自动回复
     * @return 新增/更新自动回复Id
     * */
    int createFollowReply(long weixinAppid, ResponseType responseType,
                             ResponseJson responseJson);

    /**
     * 获取被关注自动回复内容
     * @return 自动回复内容PO对象
     * */
    FollowReplyPO getFollowReply(long weixinAppid);

    /**
     * 删除被关注自动回复
     * */
    void deleteFollowReply(long weixinAppid);

    /**
     * 创建/更新消息自动回复
     * @return 新增/更新自动回复Id
     * */
    int createMsgReply(long weixinAppid, ResponseType responseType,
                        ResponseJson responseJson);

    /**
     * 获取消息自动回复内容
     * @return 自动回复内容PO对象
     * */
    MsgReplyPO getMsgReply(long weixinAppid);

    /**
     * 删除消息自动回复
     * */
    void deleteMsgReply(long weixinAppid);

    /**
     * 提供关键词自动回复回调接口
     * @return 自动回复消息内容ResponseJson
     * */
    ResponseJson getKeywordResponse(String keyword, long weixinAppid);

    /**
     * 提供被关注自动回复回调接口
     * @return 自动回复消息内容ResponseJson
     * */
    ResponseJson getFollowResponse(long weixinAppid);

    /**
     * 提供消息自动回复回调接口
     * @return 自动回复消息内容ResponseJson
     * */
    ResponseJson getMsgResponse(long weixinAppid);
}
