package com.kuaizhan.dao.redis.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuaizhan.config.ApplicationConfig;
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
        String key = ApplicationConfig.KEY_FAN_LIST + siteId;
        String result = getData(key, field);
        if (result != null) {
            List<FanDO> fanses = JsonUtil.string2List(result, FanDO.class);
            return fanses;
        }
        return null;
    }

    @Override
    public void setFanByPagination(long siteId, String field, List<FanDO> fanses) throws JsonProcessingException {
        String key = ApplicationConfig.KEY_FAN_LIST + siteId;
        String json = JsonUtil.bean2String(fanses);
        setData(key, field, json, 2 * 60 * 60);
    }

    @Override
    public boolean existFanByPagination(long siteId, String field) {
        return false;
    }

    @Override
    public void deleteFanByPagination(long siteId) {

    }

    @Override
    public List<TagDTO> listTags(long siteId) throws IOException {
        return null;
    }

    @Override
    public void setTag(long siteId, List<TagDTO> tags) throws JsonProcessingException {

    }

    @Override
    public boolean existTag(long siteId) {
        return false;
    }

    @Override
    public void deleteTag(long siteId) {

    }
}
