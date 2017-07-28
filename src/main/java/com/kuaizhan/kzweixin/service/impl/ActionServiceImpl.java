package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.cache.ActionCache;
import com.kuaizhan.kzweixin.dao.mapper.auto.ActionMapper;
import com.kuaizhan.kzweixin.dao.po.auto.ActionPO;
import com.kuaizhan.kzweixin.dao.po.auto.ActionPOExample;
import com.kuaizhan.kzweixin.entity.action.ActionResponse;
import com.kuaizhan.kzweixin.entity.action.NewsResponse;
import com.kuaizhan.kzweixin.entity.action.TextResponse;
import com.kuaizhan.kzweixin.enums.ActionType;
import com.kuaizhan.kzweixin.enums.BizCode;
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
    @Resource
    private ActionCache actionCache;


    @Override
    public int addAction(long weixinAppid, ActionPO action, ActionResponse actionResponse) {

        accountService.getAccountByWeixinAppId(weixinAppid);

        action.setWeixinAppid(weixinAppid);
        action.setCreateTime(DateUtil.curSeconds());
        action.setUpdateTime(DateUtil.curSeconds());
        action.setExt(action.getExt() == null ? "": action.getExt());
        action.setStatus(true);

        String responseJson = JsonUtil.bean2String(actionResponse);
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
        actionPO.setUpdateTime(DateUtil.curSeconds());
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

    @Override
    public ActionResponse getActionResponse(ActionPO actionPO, String openId) {
        ResponseType responseType = ResponseType.fromValue(actionPO.getResponseType());
        if (responseType == ResponseType.TEXT) {
            return JsonUtil.string2Bean(actionPO.getResponseJson(), TextResponse.class);
        } else if (responseType == ResponseType.NEWS) {
            NewsResponse newsResponse = JsonUtil.string2Bean(actionPO.getResponseJson(), NewsResponse.class);
            BizCode bizCode = BizCode.fromValue(actionPO.getBizCode());
            return addOpenIdForNewsResponse(bizCode, newsResponse, openId);
        }
        return null;
    }

    @Override
    public String getOpenIdByToken(String token) {
        String openId = actionCache.getOpenId(token);
        if (openId != null) {
            // 只允许被查询一次
            actionCache.deleteOpenId(token);
        }
        return openId;
    }

    private NewsResponse addOpenIdForNewsResponse(BizCode bizCode, NewsResponse newsResponse, String openId) {
        // 目前只有投票需要
        if (bizCode == BizCode.VOTE) {
            String url = newsResponse.getNews().get(0).getUrl();
            String separator = url.contains("?")? "&" : "?";

            int expireIn = 24 * 60 * 60;
            String token = actionCache.setOpenId(openId, expireIn);
            url = url + separator + "token=" + token + "&timestamp=" + DateUtil.curSeconds() + "&expireIn=" + expireIn;
            newsResponse.getNews().get(0).setUrl(url);
        }
        return newsResponse;
    }
}
