package com.kuaizhan.kzweixin.service;

import com.kuaizhan.kzweixin.controller.param.KeywordParamItem;
import com.kuaizhan.kzweixin.entity.responsejson.ResponseJson;
import com.kuaizhan.kzweixin.enums.ComponentResponseType;
import com.kuaizhan.kzweixin.dao.po.auto.KeywordPO;

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
    int createRule(long weixinAppid, String ruleName, List<KeywordParamItem> keywords,
                   ComponentResponseType responseType, ResponseJson responseJson);

    /**
     * 获取规则列表
     * @param query 规则名及关键词匹配
     * @return 规则列表
     * */
    List<KeywordPO> getRules(long weixinAppid, String query);

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
}
