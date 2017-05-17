package com.kuaizhan.service.impl;


import com.kuaizhan.config.KzApiConfig;
import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.dao.mapper.FanDao;
import com.kuaizhan.dao.mapper.MsgDao;
import com.kuaizhan.dao.mapper.auto.WeixinMsgConfigMapper;
import com.kuaizhan.exception.deprecated.business.SendCustomMsgException;
import com.kuaizhan.exception.common.DaoException;
import com.kuaizhan.exception.common.RedisException;
import com.kuaizhan.pojo.po.AccountPO;
import com.kuaizhan.pojo.po.FanPO;
import com.kuaizhan.pojo.po.MsgPO;
import com.kuaizhan.pojo.dto.Page;
import com.kuaizhan.pojo.po.auto.WeixinMsgConfig;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.MsgService;
import com.kuaizhan.service.WeixinMsgService;
import com.kuaizhan.utils.DBTableUtil;
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
    private MsgDao msgDao;
    @Resource
    private WeixinMsgConfigMapper msgConfigMapper;
    @Resource
    private AccountService accountService;
    @Resource
    private FanDao fanDao;
    @Resource
    private WeixinMsgService weixinMsgService;

    /** 获取消息的最后阅读时间，不存在则新建 **/
    private int getLastReadTime(long weixinAppid) {
        WeixinMsgConfig config = msgConfigMapper.selectByPrimaryKey(weixinAppid);
        // 不存在则新建
        if (config == null) {
            config = new WeixinMsgConfig();
            config.setWeixinAppid(weixinAppid);
            config.setQuickReplyJson("[]");
            int now = (int) (System.currentTimeMillis() / 1000);
            config.setLastReadTime(now);
            config.setCreateTime(now);
            config.setCreateTime(now);
            msgConfigMapper.insert(config);
        }
        return config.getLastReadTime();
    }

    @Override
    public long getUnreadMsgCount(long weixinAppid) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String appId = accountPO.getAppId();
        long lastReadTime = getLastReadTime(weixinAppid);

        String msgTableName = DBTableUtil.getMsgTableName(appId);
        return msgDao.countMsgs(appId, msgTableName, null, 1,null, false, lastReadTime, null);
    }

    @Override
    public void markMsgRead(long weixinAppid) {
        WeixinMsgConfig config = new WeixinMsgConfig();
        config.setWeixinAppid(weixinAppid);
        config.setLastReadTime((int) (System.currentTimeMillis() / 1000));
        msgConfigMapper.updateByPrimaryKeySelective(config);
    }

    /**
     * 封装消息列表的用户信息
     */
    private void setMsgUserInfo(String appId, List<MsgPO> msgPOS, AccountPO accountPO) {

        String fanTableName = DBTableUtil.getFanTableName(appId);

        Map<String, FanPO> openIdMap = new HashMap<>();
        List<String> openIds = new ArrayList<>();
        for(MsgPO msgPO: msgPOS) {
            openIds.add(msgPO.getOpenId());
        }

        // 查询粉丝信息
        if (accountPO.getServiceType() == 2 && openIds.size() > 0) {
            List<FanPO> fanPOS = fanDao.listFansByOpenIds(appId, openIds, fanTableName);
            for (FanPO fanPO: fanPOS) {
                openIdMap.put(fanPO.getOpenId(), fanPO);
            }
        }

        // 封装消息的头像和昵称信息
        for (MsgPO msgPO: msgPOS) {
            FanPO fanPO = openIdMap.getOrDefault(msgPO.getOpenId(), null);
            // 公众号发送
            if (msgPO.getSendType() == 2) {
                msgPO.setNickName(accountPO.getNickName());
                msgPO.setHeadImgUrl(accountPO.getHeadImg());

            } else if (fanPO != null) {
                msgPO.setNickName(fanPO.getNickName());
                msgPO.setHeadImgUrl(fanPO.getHeadImgUrl());

            } else {
                String openId = msgPO.getOpenId();
                msgPO.setNickName("粉丝" + openId.substring(openId.length() - 4, openId.length()));
                msgPO.setHeadImgUrl(KzApiConfig.getResUrl("/res/weixin/images/kuaizhan-logo.png"));
            }
        }
    }

    @Override
    public Page<MsgPO> listMsgsByPagination(long weixinAppid, String queryStr, boolean filterKeywords, int pageNum) {

        Page<MsgPO> page = new Page<>(pageNum, AppConstant.PAGE_SIZE_LARGE);
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);

        long lastReadTime = getLastReadTime(weixinAppid);
        String appId = accountPO.getAppId();
        String msgTableName = DBTableUtil.getMsgTableName(appId);

        // 查询消息列表
        List<MsgPO> msgPOS = msgDao.listMsgsByPagination(appId, msgTableName, null, 1,
                queryStr, filterKeywords,
                lastReadTime, page.getOffset(), page.getLimit());
        // 查询消息总数
        long count = msgDao.countMsgs(appId, msgTableName, null, 1,
                queryStr, filterKeywords, null, lastReadTime);
        page.setTotalCount(count);

        // 封装粉丝信息
        setMsgUserInfo(appId, msgPOS, accountPO);

        page.setResult(msgPOS);
        return page;
    }

    @Override
    public Page<MsgPO> listMsgsByOpenId(long weixinAppid, String openId, int pageNum) {
        Page<MsgPO> page = new Page<>(pageNum, AppConstant.PAGE_SIZE_LARGE);
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);

        String appId = accountPO.getAppId();
        String msgTableName = DBTableUtil.getMsgTableName(appId);

        // 查询消息列表
        List<MsgPO> msgPOS = msgDao.listMsgsByPagination(appId, msgTableName, openId,
                null, null, null, null,
                page.getOffset(), page.getLimit());
        // 查询消息总数
        long count = msgDao.countMsgs(appId, msgTableName, openId, 1,
                null, null, null, null);
        page.setTotalCount(count);

        // 封装粉丝信息
        setMsgUserInfo(appId, msgPOS, accountPO);

        page.setResult(msgPOS);
        return page;
    }


    @Override
    public void insertMsg(long siteId, String appId, MsgPO msgPO) throws DaoException, RedisException {
        String tableName = DBTableUtil.chooseMsgTable(System.currentTimeMillis());
        try {
            msgDao.insertMsg(tableName, msgPO);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void insertCustomMsg(AccountPO accountPO, String openId, int msgType, JSONObject content) throws SendCustomMsgException, DaoException, RedisException {
        weixinMsgService.sendCustomMsg(accountPO.getAppId(), accountPO.getAccessToken(), openId, msgType, content);
        MsgPO msg = new MsgPO();
        msg.setAppId(accountPO.getAppId());
        msg.setContent(content.toString());
        msg.setOpenId(openId);
        msg.setType(msgType);
        msg.setSendType(2);
//        msg.setStatus(2);
        try {
            msgDao.insertMsg(DBTableUtil.chooseMsgTable(System.currentTimeMillis()), msg);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

}
