package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.dao.mapper.auto.ActionMapper;
import com.kuaizhan.kzweixin.dao.po.auto.ActionPO;
import com.kuaizhan.kzweixin.dao.po.auto.ActionPOExample;
import com.kuaizhan.kzweixin.entity.action.NewsResponse;
import com.kuaizhan.kzweixin.entity.action.TextResponse;
import com.kuaizhan.kzweixin.enums.ActionType;
import com.kuaizhan.kzweixin.enums.ResponseType;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.ActionService;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zixiong on 2017/6/26.
 */
@Service
public class ActionServiceImpl implements ActionService {

    @Resource
    private AccountService accountService;
    @Resource
    private ActionMapper actionMapper;


    @Override
    public int addAction(long weixinAppid, ActionPO action, Object responseObj) {

        accountService.getAccountByWeixinAppId(weixinAppid);

        action.setWeixinAppid(weixinAppid);
        action.setExt(action.getExt() == null? "": action.getExt());
        action.setCreateTime(DateUtil.curSeconds());
        action.setUpdateTime(DateUtil.curSeconds());
        action.setExt("");
        action.setStatus(true);

        int responseType = action.getResponseType();
        if (responseType == ResponseType.TEXT.getValue()) {
            if (!(responseObj instanceof TextResponse)) {
                throw new IllegalArgumentException("illegal response type, should be TextResponse");
            }
        } else if (responseType == ResponseType.NEWS.getValue()) {
            if (!(responseObj instanceof NewsResponse)) {
                throw new IllegalArgumentException("illegal response type, should be NewsResponse");
            }
        } else {
            throw new IllegalArgumentException("unsupported responseType:" + responseType);
        }

        String responseJson = JsonUtil.bean2String(responseObj);
        checkResponseJson(responseJson);

        action.setResponseJson(responseJson);
        actionMapper.insert(action);
        return action.getId();
    }

    /**
     * 数据库字段限制，目前不能大于2000
     */
    private void checkResponseJson(String responseJson) {
        if (responseJson == null || responseJson.length() > 2000) {
            throw new RuntimeException("[checkResponseJson] invalid responseJson length:"
                    + (responseJson == null? 0: responseJson.length()));
        }
    }

    @Override
    public void updateAction(ActionPO actionPO) {
        if (actionPO.getId() == null) {
            throw new IllegalArgumentException("actionId can not be null");
        }
        actionMapper.updateByPrimaryKeySelective(actionPO);
    }

    @Override
    public ActionPO getActionById(int actionId) {
        // 不论启用状态，直接返回
        return actionMapper.selectByPrimaryKey(actionId);
    }

    @Override
    public List<ActionPO> getActions(long weixinAppid, ActionType actionType) {
        ActionPOExample example = new ActionPOExample();
        example.createCriteria()
                .andWeixinAppidEqualTo(weixinAppid)
                .andActionTypeEqualTo(actionType.getValue())
                .andStatusEqualTo(true);
        example.setOrderByClause("create_time DESC");
        return actionMapper.selectByExample(example);
    }

    @Override
    public boolean shouldAction(ActionPO actionPO, String keyword) {
        // 订阅类型，都触发
        if (actionPO.getActionType() == ActionType.SUBSCRIBE.getValue()) {
            return true;
        }
        // 回复类型，正则匹配keyword时触发
        if (actionPO.getActionType() == ActionType.REPLY.getValue()) {
            return keyword != null && keyword.matches(actionPO.getKeyword());
        }
        return false;
    }
}
