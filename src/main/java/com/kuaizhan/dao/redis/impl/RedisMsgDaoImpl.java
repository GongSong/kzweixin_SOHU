package com.kuaizhan.dao.redis.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuaizhan.config.ApplicationConfig;
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
        String key = ApplicationConfig.KEY_MSG_LIST + siteId;
        String result = getData(key, field);
        if (result != null) {
            List<MsgDO> msgs = JsonUtil.string2List(result, MsgDO.class);
            return msgs;
        }
        return null;
    }

    @Override
    public void setMsgsByPagination(long siteId, String field, List<MsgDO> msgs) throws JsonProcessingException {
        String key = ApplicationConfig.KEY_MSG_LIST + siteId;
        String json = JsonUtil.bean2String(msgs);
        setData(key, field, json, 2 * 60 * 60);
    }

    @Override
    public void deleteMsgsByPagination(long siteId) {
        String key = ApplicationConfig.KEY_MSG_LIST + siteId;
        deleteData(key);
    }

}
