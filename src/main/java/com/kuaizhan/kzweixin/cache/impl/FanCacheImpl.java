package com.kuaizhan.kzweixin.cache.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuaizhan.kzweixin.constant.RedisConstant;
import com.kuaizhan.kzweixin.cache.FanCache;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import com.kuaizhan.kzweixin.entity.fan.TagDTO;
import org.springframework.stereotype.Repository;
import com.kuaizhan.kzweixin.dao.po.auto.FanPO;

import java.io.IOException;
import java.util.List;

/**
 * Created by liangjiateng on 2017/3/16.
 */
@Repository("fanCache")
public class FanCacheImpl extends RedisBaseDaoImpl implements FanCache {


    @Override
    public List<FanPO> listFanByPagination(long siteId, String field) throws IOException {
        String key = RedisConstant.KEY_FAN_LIST + siteId;
        String result = getData(key, field);
        if (result != null) {
            List<FanPO> fanses = JsonUtil.string2List(result, FanPO.class);
            return fanses;
        }
        return null;
    }

    @Override
    public void setFanByPagination(long siteId, String field, List<FanPO> fanses) throws JsonProcessingException {
        String key = RedisConstant.KEY_FAN_LIST + siteId;
        String json = JsonUtil.bean2String(fanses);
        setData(key, field, json, 2 * 60 * 60);
    }


    @Override
    public void deleteFanByPagination(long siteId) {
        deleteData(RedisConstant.KEY_FAN_LIST + siteId);
    }

    @Override
    public void deleteTags(long weixinAppid) {
        String key = RedisConstant.KEY_TAG + weixinAppid;
        deleteData(key);
    }

    @Override
    public List<TagDTO> getTags(long weixinAppid) {
        String key = RedisConstant.KEY_TAG + weixinAppid;
        String result = getData(key);
        if (result == null) {
            return null;
        }
        List<TagDTO> tagsList = JsonUtil.string2List(result, TagDTO.class);
        if (tagsList != null && tagsList.size() != 0) {
            return tagsList;
        }
        return null;
    }

    @Override
    public void setTag(long weixinAppid, List<TagDTO> tagsList) throws JsonProcessingException {
        String key = RedisConstant.KEY_TAG + weixinAppid;
        String json = JsonUtil.list2Str(tagsList);
        setData(key, json, 10 * 60 * 60);
    }
}
