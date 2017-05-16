package com.kuaizhan.service.impl;


import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.dao.mapper.FanDao;
import com.kuaizhan.dao.mapper.MsgDao;
import com.kuaizhan.dao.redis.RedisMsgDao;
import com.kuaizhan.exception.deprecated.business.SendCustomMsgException;
import com.kuaizhan.exception.common.DaoException;
import com.kuaizhan.exception.common.RedisException;
import com.kuaizhan.pojo.po.AccountPO;
import com.kuaizhan.pojo.po.FanPO;
import com.kuaizhan.pojo.po.MsgPO;
import com.kuaizhan.pojo.dto.Page;
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
    MsgDao msgDao;
    @Resource
    RedisMsgDao redisMsgDao;
    @Resource
    FanDao fanDao;
    @Resource
    WeixinMsgService weixinMsgService;

    @Override
    public long countMsg(String appId, int status, int sendType, String keyword, int isHide) throws DaoException {
        if (keyword == null) {
            keyword = "";
        }
        List<String> tableNames = DBTableUtil.getMsgTableNames();
        long total = 0;
        try {
            List<Long> counts = msgDao.count(appId, sendType, status, keyword, isHide, tableNames);
            for (Long count : counts) {
                total += count;
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return total;
    }

    @Override
    public Page<MsgPO> listMsgsByPagination(long siteId, String appId, int page, String keyword, int isHide) throws DaoException, RedisException {
        if (keyword == null)
            keyword = "";
        String field = "page:" + page + "keyword:" + keyword + "isHide:" + isHide;
        Page<MsgPO> pagingResult = new Page<>(page, AppConstant.PAGE_SIZE_LARGE);
        pagingResult.setTotalCount(countMsg(appId, 2, 1, keyword, isHide));

        //从redis拿数据
        try {
            List<MsgPO> msgPOList = redisMsgDao.listMsgsByPagination(siteId, field);
            if (msgPOList != null) {
                pagingResult.setResult(msgPOList);
                return pagingResult;
            }
        } catch (Exception e) {
            throw new RedisException(e);
        }

        //未命中,从数据库拿数据,并且缓存到redis,缓存两个小时
        Map<String, Object> map = new HashMap<>();
        map.put("appId", appId);
        map.put("status", 2);
        map.put("sendType", 1);
        map.put("keyword", keyword);
        map.put("isHide", isHide);
        pagingResult.setParams(map);

        List<String> msgTableNames = DBTableUtil.getMsgTableNames();
        List<String> openIds = new ArrayList<>();
        List<MsgPO> msgs;
        try {
            msgs = msgDao.listMsgsByPagination(msgTableNames, pagingResult);
        } catch (Exception e) {
            throw new DaoException(e);
        }
        for (MsgPO msgPO : msgs) {
            openIds.add(msgPO.getOpenId());
        }

        List<String> fansTableNames = DBTableUtil.getFanTableNames();

        if (openIds.size() > 0) {

            List<FanPO> fanPOList;
            try {
                fanPOList = fanDao.listFansByOpenIds(appId, openIds, fansTableNames);
            } catch (Exception e) {
                throw new DaoException(e);
            }

            for (MsgPO msgPO : msgs) {
                for (FanPO fanPO : fanPOList) {
                    if (msgPO.getOpenId().equals(fanPO.getOpenId())) {
                        msgPO.setNickName(fanPO.getNickName());
                        msgPO.setHeadImgUrl(fanPO.getHeadImgUrl());
                        msgPO.setIsFocus(fanPO.getStatus());
                    }
                }
            }

            if (msgs.size() > 20) {
                try {
                    redisMsgDao.setMsgsByPagination(siteId, field, msgs.subList(0, 20));
                    pagingResult.setResult(msgs.subList(0, 20));
                } catch (Exception e) {
                    throw new RedisException(e);
                }
            } else {
                try {
                    redisMsgDao.setMsgsByPagination(siteId, field, msgs.subList(0, msgs.size()));
                    pagingResult.setResult(msgs.subList(0, msgs.size()));
                } catch (Exception e) {
                    throw new RedisException(e);
                }
            }
        }
        return pagingResult;
    }

    @Override
    public List<MsgPO> listNewMsgs(String appId) throws DaoException {

        List<String> msgTableNames = DBTableUtil.getMsgTableNames();
        List<MsgPO> msgs;
        try {
            msgs = msgDao.listNewMsgs(appId, msgTableNames);
        } catch (Exception e) {
            throw new DaoException(e);
        }

        List<String> openIds = new ArrayList<>();
        for (MsgPO msg : msgs) {
            openIds.add(msg.getOpenId());
        }
        List<String> fansTableNames = DBTableUtil.getFanTableNames();

        if (openIds.size() > 0) {
            try {
                List<FanPO> fanPOList = fanDao.listFansByOpenIds(appId, openIds, fansTableNames);
                for (MsgPO msgPO : msgs) {
                    for (FanPO fanPO : fanPOList) {
                        if (msgPO.getOpenId().equals(fanPO.getOpenId())) {
                            msgPO.setNickName(fanPO.getNickName());
                            msgPO.setHeadImgUrl(fanPO.getHeadImgUrl());
                        }
                    }
                }
            } catch (Exception e) {
                throw new DaoException(e);
            }
        }

        return msgs;
    }

    @Override
    public Page<MsgPO> listMsgsByOpenId(long siteId, String appId, String openId, int page) throws RedisException, DaoException {
        Page<MsgPO> pageEntity = new Page<>(page, AppConstant.PAGE_SIZE_MIDDLE);
        try {
            List<MsgPO> msgPOList = redisMsgDao.listMsgsByOpenId(siteId, openId, page);
            if (msgPOList != null) {
                pageEntity.setResult(msgPOList);
                return pageEntity;
            }
        } catch (Exception e) {
            throw new RedisException(e);
        }

        List<String> msgTableNames = DBTableUtil.getMsgTableNames();

        Map<String, Object> param = new HashMap<>();
        param.put("appId", appId);
        param.put("openId", openId);
        pageEntity.setParams(param);
        List<MsgPO> msgs;
        try {
            msgs = msgDao.listMsgsByOpenId(pageEntity, msgTableNames);
        } catch (Exception e) {
            throw new DaoException(e);
        }
        try {
            if (msgs.size() > 20) {
                //缓存2个小时
                redisMsgDao.setMsgsByOpenId(siteId, openId, page, msgs.subList(0, 20));
                pageEntity.setResult(msgs.subList(0, 20));
                return pageEntity;
            } else {
                //缓存2个小时
                redisMsgDao.setMsgsByOpenId(siteId, openId, page, msgs);
                pageEntity.setResult(msgs);
                return pageEntity;
            }
        } catch (Exception e) {
            throw new RedisException(e);
        }
    }

    @Override
    public void updateMsgsStatus(long siteId, String appId, List<MsgPO> msgs) throws DaoException, RedisException {
        for (MsgPO msg : msgs) {
            msg.setStatus(2);
        }
        List<String> msgTableNames = DBTableUtil.getMsgTableNames();
        try {
            msgDao.updateMsgBatch(msgTableNames, msgs);
        } catch (Exception e) {
            throw new DaoException(e);
        }
        try {
            //删除redis
            redisMsgDao.deleteMsgsByPagination(siteId);
        } catch (Exception e) {
            throw new RedisException(e);
        }

    }

    @Override
    public void insertMsg(long siteId, String appId, MsgPO msgPO) throws DaoException, RedisException {
        String tableName = DBTableUtil.chooseMsgTable(System.currentTimeMillis());
        try {
            msgDao.insertMsg(tableName, msgPO);
        } catch (Exception e) {
            throw new DaoException(e);
        }
        //删除缓存
        try {
            redisMsgDao.deleteMsgsByPagination(siteId);
            redisMsgDao.deleteMsgsByOpenId(siteId, msgPO.getOpenId());
        } catch (Exception e) {
            throw new RedisException(e);
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
        msg.setStatus(2);
        try {
            msgDao.insertMsg(DBTableUtil.chooseMsgTable(System.currentTimeMillis()), msg);
        } catch (Exception e) {
            throw new DaoException(e);
        }
        try {
            //清除redis
            redisMsgDao.deleteMsgsByOpenId(accountPO.getSiteId(), openId);
        } catch (Exception e) {
            throw new RedisException(e);
        }

    }

}
