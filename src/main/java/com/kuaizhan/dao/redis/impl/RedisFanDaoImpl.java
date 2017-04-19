package com.kuaizhan.dao.redis.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuaizhan.constant.RedisConstant;
import com.kuaizhan.dao.redis.RedisFanDao;
import com.kuaizhan.pojo.DO.FanDO;
import com.kuaizhan.pojo.DTO.TagDTO;
import com.kuaizhan.utils.JsonUtil;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

/**
 * Created by liangjiateng on 2017/3/16.
 */
@Repository("redisFanDaoImpl")
public class RedisFanDaoImpl extends RedisBaseDaoImpl implements RedisFanDao {


    @Override
    public List<FanDO> listFanByPagination(long siteId, String field) throws IOException {
        String key = RedisConstant.KEY_FAN_LIST + siteId;
        String result = getData(key, field);
        if (result != null) {
            List<FanDO> fanses = JsonUtil.string2List(result, FanDO.class);
            return fanses;
        }
        return null;
    }

    @Override
    public void setFanByPagination(long siteId, String field, List<FanDO> fanses) throws JsonProcessingException {
        String key = RedisConstant.KEY_FAN_LIST + siteId;
        String json = JsonUtil.bean2String(fanses);
        setData(key, field, json, 2 * 60 * 60);
    }


    @Override
    public void deleteFanByPagination(long siteId) {
        deleteData(RedisConstant.KEY_FAN_LIST + siteId);
    }

    @Override
    public List<TagDTO> listTags(long siteId) throws IOException {
        String key = RedisConstant.KEY_TAG + siteId;
        String result = getData(key);
        if (result != null) {
            return JsonUtil.string2List(result, TagDTO.class);
        }
        return null;
    }

    @Override
    public void setTag(long siteId, List<TagDTO> tags) throws JsonProcessingException {
        String key = RedisConstant.KEY_TAG + siteId;
        String json = JsonUtil.bean2String(tags);
        setData(key, json, 10 * 60 * 60);
    }


    @Override
    public void deleteTag(long siteId) {
        String key = RedisConstant.KEY_TAG + siteId;
        deleteData(key);
    }
}
