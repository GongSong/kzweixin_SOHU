package com.kuaizhan.dao.redis.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuaizhan.constant.RedisConstant;
import com.kuaizhan.dao.redis.RedisMsgDao;
import com.kuaizhan.pojo.DO.MsgDO;
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
    public List<MsgDO> listMsgsByPagination(long siteId, String field) throws IOException {
        String key = RedisConstant.KEY_MSG_LIST + siteId;
        String result = getData(key, field);
        if (result != null) {
            return JsonUtil.string2List(result, MsgDO.class);
        }
        return null;
    }

    @Override
    public void setMsgsByPagination(long siteId, String field, List<MsgDO> msgs) throws JsonProcessingException {
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
    public List<MsgDO> listMsgsByOpenId(long siteId, String openId, int page) throws IOException {
        String key = RedisConstant.KEY_MSG_USER + siteId + openId;
        String field = "page:" + page;
        String result = getData(key, field);
        if (result != null) {
            List<MsgDO> msgs = JsonUtil.string2List(result, MsgDO.class);
            return msgs;
        }
        return null;
    }

    @Override
    public void setMsgsByOpenId(long siteId, String openId, int page, List<MsgDO> msgs) throws JsonProcessingException {
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
