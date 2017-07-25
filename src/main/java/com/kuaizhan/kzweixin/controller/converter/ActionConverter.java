package com.kuaizhan.kzweixin.controller.converter;

import com.kuaizhan.kzweixin.controller.vo.ActionVO;
import com.kuaizhan.kzweixin.dao.po.auto.ActionPO;
import com.kuaizhan.kzweixin.utils.JsonUtil;

import java.util.Map;

/**
 * Created by zixiong on 2017/07/25.
 */
public class ActionConverter {

    public static ActionVO toActionVo(ActionPO actionPO) {
        if (actionPO == null) {
            return null;
        }
        ActionVO actionVO = new ActionVO();
        actionVO.setId(actionPO.getId());
        actionVO.setBizCode(actionPO.getBizCode());
        actionVO.setActionType(actionPO.getActionType());
        actionVO.setKeyword(actionPO.getKeyword());
        actionVO.setResponseType(actionPO.getResponseType());
        Map responseJson = JsonUtil.string2Bean(actionPO.getResponseJson(), Map.class);
        actionVO.setResponseJson(responseJson);
        actionVO.setStatus(actionPO.getStatus());

        return actionVO;
    }
}
