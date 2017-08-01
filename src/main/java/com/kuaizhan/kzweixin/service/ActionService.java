package com.kuaizhan.kzweixin.service;

import com.kuaizhan.kzweixin.dao.po.auto.ActionPO;
import com.kuaizhan.kzweixin.entity.wxresponse.CallbackResponse;
import com.kuaizhan.kzweixin.enums.ActionType;

import java.util.List;

/**
 * Created by zixiong on 2017/6/26.
 */
public interface ActionService {
    /**
     * 新增Action
     */
    int addAction(long weixinAppid, ActionPO action, CallbackResponse callbackResponse);

    /**
     * 修改Action
     * @param actionPO action对象
     */
    void updateAction(ActionPO actionPO);

    /**
     * 获取action详情
     */
    ActionPO getActionById(int actionId);

    /**
     * 获取指定类型action列表
     */
    List<ActionPO> getActions(long weixinAppid, ActionType actionType);


    /**
     * 根据actionType和keyword判断是否该触发action
     */
    boolean shouldAction(ActionPO actionPO, String keyword);

    /**
     * 根据actionPO对象获取ActionResponse
     * @param openId news消息类型，有可能会在url带上openId
     */
    CallbackResponse getActionResponse(ActionPO actionPO, String openId);

    /**
     * 根据token换取
     */
    String getOpenIdByToken(String token);
}
