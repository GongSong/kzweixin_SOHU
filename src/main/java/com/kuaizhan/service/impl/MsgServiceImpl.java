package com.kuaizhan.service.impl;


import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.mapper.FanDao;
import com.kuaizhan.dao.mapper.MsgDao;
import com.kuaizhan.dao.redis.RedisMsgDao;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.pojo.DO.FanDO;
import com.kuaizhan.pojo.DO.MsgDO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.service.MsgService;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
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

    @Override
    public long countMsg(String appId, int status, int sendType, String keyword, int isHide) throws DaoException {
        if (keyword == null) {
            keyword = "";
        }
        List<String> tableNames = ApplicationConfig.getMsgTableNames();
        long total = 0;
        try {
            List<Long> counts = msgDao.count(appId,  sendType,status, keyword, isHide, tableNames);
            for (Long count : counts) {
                total += count;
            }
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
        return total;
    }

    @Override
    public Page<MsgDO> listMsgsByPagination(long siteId, String appId, int page, String keyword, int isHide) throws DaoException, RedisException {
        if (keyword == null)
            keyword = "";
        String field = "page:" + page + "keyword:" + keyword + "isHide:" + isHide;
        Page<MsgDO> pagingResult = new Page<>(page, ApplicationConfig.PAGE_SIZE_LARGE);
        pagingResult.setTotalCount(countMsg(appId, 2,1, keyword, isHide));

        //从redis拿数据
        try {
            List<MsgDO> msgDOList = redisMsgDao.listMsgsByPagination(siteId, field);
            if (msgDOList != null) {
                pagingResult.setResult(msgDOList);
                return pagingResult;
            }
        } catch (Exception e) {
            throw new RedisException(e.getMessage());
        }

        //未命中,从数据库拿数据,并且缓存到redis,缓存两个小时
        Map<String, Object> map = new HashMap<>();
        map.put("appId", appId);
        map.put("status", 2);
        map.put("sendType", 1);
        map.put("keyword", keyword);
        map.put("isHide", isHide);
        pagingResult.setParams(map);

        List<String> msgTableNames = ApplicationConfig.getMsgTableNames();
        List<String> openIds = new ArrayList<>();
        List<MsgDO> msgs;
        try {
            msgs = msgDao.listMsgsByPagination(msgTableNames, pagingResult);
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
        for (MsgDO msgDO : msgs) {
            openIds.add(msgDO.getOpenId());
        }

        List<String> fansTableNames = ApplicationConfig.getFanTableNames();

        if (openIds.size() > 0) {

            List<FanDO> fanDOList;
            try {
                fanDOList = fanDao.listFansByOpenIds(appId, openIds, fansTableNames);
            } catch (Exception e) {
                throw new DaoException(e.getMessage());
            }

            for (MsgDO msgDO : msgs) {
                for (FanDO fanDO : fanDOList) {
                    if (msgDO.getOpenId().equals(fanDO.getOpenId())) {
                        msgDO.setNickName(fanDO.getNickName());
                        msgDO.setHeadImgUrl(fanDO.getHeadImgUrl());
                        msgDO.setIsFoucs(fanDO.getStatus());
                    }
                }
            }

            if (msgs.size() > 20) {
                try {
                    redisMsgDao.setMsgsByPagination(siteId, field, msgs.subList(0, 20));
                    pagingResult.setResult(msgs.subList(0, 20));
                } catch (Exception e) {
                    throw new RedisException(e.getMessage());
                }
            } else {
                try {
                    redisMsgDao.setMsgsByPagination(siteId, field, msgs.subList(0, msgs.size()));
                    pagingResult.setResult(msgs.subList(0, msgs.size()));
                } catch (Exception e) {
                    throw new RedisException(e.getMessage());
                }
            }
        }
        return pagingResult;
    }

    @Override
    public List<MsgDO> listNewMsgs(String appId) {
        return null;
    }

    @Override
    public List<MsgDO> listMsgsByOpenId(String appId, String openId, int page) throws IOException {
        return null;
    }

    @Override
    public void updateMsgsStatus(String appId, List<MsgDO> msgs) {

    }

    @Override
    public int sendMsgByOpenId(String appId, String accessToken, String openId, int msgType, JSONObject content) {
        return 0;
    }
}
