package com.kuaizhan.kzweixin.service.impl;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.cache.MsgCache;
import com.kuaizhan.kzweixin.dao.mapper.auto.MsgMapper;
import com.kuaizhan.kzweixin.dao.po.auto.*;
import com.kuaizhan.kzweixin.entity.common.PageV2;
import com.kuaizhan.kzweixin.entity.msg.MsgLinkGroupResponseJson;
import com.kuaizhan.kzweixin.entity.responsejson.LinkGroupResponseJson;
import com.kuaizhan.kzweixin.entity.responsejson.PostResponseJson;
import com.kuaizhan.kzweixin.entity.responsejson.ResponseJson;
import com.kuaizhan.kzweixin.enums.MsgSendType;
import com.kuaizhan.kzweixin.enums.MsgType;
import com.kuaizhan.kzweixin.dao.mapper.auto.MsgConfigMapper;
import com.kuaizhan.kzweixin.manager.KzManager;
import com.kuaizhan.kzweixin.manager.WxMsgManager;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.MsgService;
import com.kuaizhan.kzweixin.service.PostService;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 消息管理服务
 * Created by Mr.Jadyn on 2017/1/10.
 */
@Service("msgService")
public class MsgServiceImpl implements MsgService {

    @Resource
    private MsgMapper msgMapper;
    @Resource
    private MsgConfigMapper msgConfigMapper;
    @Resource
    private AccountService accountService;
    @Resource
    private MsgCache msgCache;

    @Override
    public long getUnreadMsgCount(long weixinAppid) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);

        int lastReadTime = getLastReadTime(weixinAppid);

        MsgPOExample example = new MsgPOExample();
        example.createCriteria()
                .andAppIdEqualTo(accountPO.getAppId())
                .andSendTypeEqualTo(MsgSendType.TO_ACCOUNT)
                .andCreateTimeGreaterThan(lastReadTime);

        return msgMapper.countByExample(example);
    }

    @Override
    public void markMsgRead(long weixinAppid) {
        MsgConfigPO config = new MsgConfigPO();
        config.setWeixinAppid(weixinAppid);
        config.setLastReadTime(DateUtil.curSeconds());
        msgConfigMapper.updateByPrimaryKeySelective(config);
    }

    @Override
    public PageV2<MsgPO> listMsgsByPage(long weixinAppid, String queryStr, boolean filterKeywords, int offset, int limit) {

        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);

        int lastReadTime = getLastReadTime(weixinAppid);
        String appId = accountPO.getAppId();

        // 组装查询条件
        MsgPOExample example = new MsgPOExample();
        MsgPOExample.Criteria criteria = example.createCriteria();
        criteria.andAppIdEqualTo(appId);
        criteria.andSendTypeEqualTo(MsgSendType.TO_ACCOUNT);
        criteria.andCreateTimeLessThan(lastReadTime);
        // 模糊查询不为空
        if (!StringUtils.isBlank(queryStr)) {
            criteria.andContentLike("%" + queryStr + "%");
            criteria.andTypeIn(ImmutableList.of(MsgType.TEXT, MsgType.KEYWORD_TEXT));
        }
        // 过滤关键词
        if (filterKeywords) {
            criteria.andTypeNotEqualTo(MsgType.KEYWORD_TEXT);
        }
        example.setOrderByClause("create_time DESC");

        List<MsgPO> msgPOS = msgMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, limit));
        long total = msgMapper.countByExample(example);

        return new PageV2<>(total, msgPOS);
    }

    @Override
    public PageV2<MsgPO> listMsgsByOpenId(long weixinAppid, String openId, int offset, int limit) {

        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);

        MsgPOExample example = new MsgPOExample();
        example.createCriteria()
                .andAppIdEqualTo(accountPO.getAppId())
                .andOpenIdEqualTo(openId);
        example.setOrderByClause("create_time DESC");

        List<MsgPO> msgPOS = msgMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, limit));
        long total = msgMapper.countByExample(example);

        return new PageV2<>(total, msgPOS);
    }

    @Override
    public List<String> getQuickReplies(long weixinAppid) {
        MsgConfigPO config = msgConfigMapper.selectByPrimaryKey(weixinAppid);
        return JsonUtil.string2List(config.getQuickReplyJson(), String.class);
    }

    @Override
    public void updateQuickReplies(long weixinAppid, List<String> replies) {
        MsgConfigPO config = new MsgConfigPO();
        config.setWeixinAppid(weixinAppid);
        config.setQuickReplyJson(JsonUtil.list2Str(replies));
        msgConfigMapper.updateByPrimaryKeySelective(config);
    }

    @Override
    public void sendCustomMsg(long weixinAppid, String openId, MsgType msgType, ResponseJson responseJson) {

        // 对于发送的图文消息，转为PHP兼容的LinkGroup
        if (responseJson instanceof PostResponseJson) {
            msgType = MsgType.LINK_GROUP;
            responseJson = convert2MsgLinkGroup(responseJson);
        }
        // 对于LinkGroup， 转为PHP兼容的LinkGroup
        if (responseJson instanceof LinkGroupResponseJson) {
            responseJson = convert2MsgLinkGroup(responseJson);
        }

        // 调用微信接口发送客服消息
        String accessToken = accountService.getAccessToken(weixinAppid);
        WxMsgManager.sendCustomMsg(accessToken, openId, responseJson);

        // 入库
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String appId = accountPO.getAppId();

        MsgPO record = new MsgPO();
        record.setAppId(appId);
        record.setOpenId(openId);
        record.setSendType(MsgSendType.TO_FAN);
        record.setType(msgType);
        responseJson.cleanBeforeInsert();
        record.setContent(JsonUtil.bean2String(responseJson));
        record.setCreateTime(DateUtil.curSeconds());
        record.setUpdateTime(DateUtil.curSeconds());
        msgMapper.insertSelective(record);
    }

    /**
     * 把LinkGroupResponseJson、PostResponseJson转换为兼容的MsgLinkGroup
     */
    private MsgLinkGroupResponseJson convert2MsgLinkGroup(ResponseJson responseJson) {
        MsgLinkGroupResponseJson msgLinkGroupResponseJson = new MsgLinkGroupResponseJson();
        List<MsgLinkGroupResponseJson.LinkGroup> linkGroups = new ArrayList<>();
        msgLinkGroupResponseJson.setLinkGroups(linkGroups);

        if (responseJson instanceof PostResponseJson) {
            PostResponseJson postResponseJson = (PostResponseJson) responseJson;
            for (PostResponseJson.Post post: postResponseJson.getPosts()) {
                MsgLinkGroupResponseJson.LinkGroup msgLinkGroup = new MsgLinkGroupResponseJson.LinkGroup();
                msgLinkGroup.setTitle(post.getTitle());
                msgLinkGroup.setDescription(post.getDescription());
                msgLinkGroup.setPicUrl(post.getPicUrl());
                msgLinkGroup.setUrl(post.getUrl());
                linkGroups.add(msgLinkGroup);
            }
        } else if (responseJson instanceof LinkGroupResponseJson) {
            LinkGroupResponseJson linkGroupResponseJson = (LinkGroupResponseJson) responseJson;
            for (LinkGroupResponseJson.LinkGroup linkGroup: linkGroupResponseJson.getLinkGroups()) {
                MsgLinkGroupResponseJson.LinkGroup msgLinkGroup = new MsgLinkGroupResponseJson.LinkGroup();
                msgLinkGroup.setTitle(linkGroup.getTitle());
                msgLinkGroup.setDescription(linkGroup.getDescription());
                msgLinkGroup.setUrl(linkGroup.getUrl());
                msgLinkGroup.setPicUrl(linkGroup.getPicUrl());
                linkGroups.add(msgLinkGroup);
            }
        }
        return msgLinkGroupResponseJson;
    }

    @Override
    public String getPushToken(long weixinAppid, String openId) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String appId = accountPO.getAppId();

        String cache = msgCache.getPushToken(appId, openId);
        if (cache != null) {
            JSONObject jsonObject = new JSONObject(cache);
            return jsonObject.getString("push_token");
        }

        Map<String, String> result = KzManager.applyPushToken();
        String token = result.get("token");
        String clientId = result.get("clientId");
        // 存到redis，微信回调时根据是否有token决定是否推送
        msgCache.setPushToken(appId, openId, JsonUtil.bean2String(ImmutableMap.of("push_client_id", clientId, "push_token", token)));

        return token;
    }

    @Override
    public void deletePushToken(long weixinAppid, String openId) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        msgCache.deletePushToken(accountPO.getAppId(), openId);
    }


    /** 获取消息的最后阅读时间，不存在则新建 **/
    private int getLastReadTime(long weixinAppid) {
        MsgConfigPO config = msgConfigMapper.selectByPrimaryKey(weixinAppid);
        // 不存在则新建
        if (config == null) {
            config = new MsgConfigPO();
            config.setWeixinAppid(weixinAppid);
            config.setQuickReplyJson("[]");
            int now = DateUtil.curSeconds();
            config.setLastReadTime(now);
            config.setCreateTime(now);
            config.setUpdateTime(now);
            msgConfigMapper.insert(config);
        }
        return config.getLastReadTime();
    }
}
