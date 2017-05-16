package com.kuaizhan.dao.redis.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuaizhan.constant.RedisConstant;
import com.kuaizhan.dao.redis.RedisMsgDao;
import com.kuaizhan.pojo.po.MsgPO;
import com.kuaizhan.utils.JsonUtil;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

/**
 * Created by liangjiateng on 2017/3/18.
 */
@Repository("redisMsgDao")
public class RedisMsgDaoImpl extends RedisBaseDaoImpl implements RedisMsgDao {
    @Override
    public List<MsgPO> listMsgsByPagination(long siteId, String field) throws IOException {
        String key = RedisConstant.KEY_MSG_LIST + siteId;
        String result = getData(key, field);
        if (result != null) {
            return JsonUtil.string2List(result, MsgPO.class);
        }
        return null;
    }

    @Override
    public void setMsgsByPagination(long siteId, String field, List<MsgPO> msgs) throws JsonProcessingException {
        String key = RedisConstant.KEY_MSG_LIST + siteId;
        String json = JsonUtil.bean2String(msgs);
        setData(key, field, json, 2 * 60 * 60);
    }

    @Override
    public void deleteMsgsByPagination(long siteId) {
        String key = RedisConstant.KEY_MSG_LIST + siteId;
        deleteData(key);
    }

    @Override
    public List<MsgPO> listMsgsByOpenId(long siteId, String openId, int page) throws IOException {
        String key = RedisConstant.KEY_MSG_USER + siteId + openId;
        String field = "page:" + page;
        String result = getData(key, field);
        if (result != null) {
            List<MsgPO> msgs = JsonUtil.string2List(result, MsgPO.class);
            return msgs;
        }
        return null;
    }

    @Override
    public void setMsgsByOpenId(long siteId, String openId, int page, List<MsgPO> msgs) throws JsonProcessingException {
        String key = RedisConstant.KEY_MSG_USER + siteId + openId;
        String field = "page:" + page;
        String json = JsonUtil.bean2String(msgs);
        setData(key, field, json, 2 * 60 * 60);
    }

    @Override
    public void deleteMsgsByOpenId(long siteId, String openId) {
        String key = RedisConstant.KEY_MSG_USER + siteId + openId;
        deleteData(key);
    }

}
