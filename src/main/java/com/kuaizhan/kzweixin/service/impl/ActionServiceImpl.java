package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.entity.action.NewsResponse;
import com.kuaizhan.kzweixin.entity.action.TextResponse;
import com.kuaizhan.kzweixin.enums.ResponseType;
import com.kuaizhan.kzweixin.dao.mapper.auto.ActionMapper;
import com.kuaizhan.kzweixin.dao.po.auto.ActionPO;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.ActionService;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
        action.setCreateTime(DateUtil.curSeconds());
        action.setUpdateTime(DateUtil.curSeconds());
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
        action.setResponseJson(JsonUtil.bean2String(responseObj));
        actionMapper.insert(action);
        return action.getId();
    }

    @Override
    public void updateAction(long weixinAppid, ActionPO action, Object responseObj) {
    }
}
