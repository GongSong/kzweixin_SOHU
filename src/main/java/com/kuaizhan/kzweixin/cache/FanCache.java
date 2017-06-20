package com.kuaizhan.kzweixin.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuaizhan.kzweixin.entity.fan.TagDTO;
import com.kuaizhan.kzweixin.dao.po.auto.FanPO;

import java.io.IOException;
import java.util.List;


/**
 * Created by liangjiateng on 2017/3/16.
 */
public interface FanCache {
    /**
     * 分页查询粉丝列表
     *
     * @return
     */
    List<FanPO> listFanByPagination(long siteId, String field) throws IOException;

    /**
     * 缓存分页查询数据
     *
     * @param siteId
     * @param field
     * @param fanses
     */
    void setFanByPagination(long siteId, String field, List<FanPO> fanses) throws JsonProcessingException;

    /**
     * 删除分页缓存
     * @param siteId
     */
    void deleteFanByPagination(long siteId);

    /**
     * 清除tag
     */
    void deleteTags(long weixinAppid);

    /**
     * 获取所有标签
     * @return
     */
    List<TagDTO> getTags(long weixinAppid);

    /**
     * 缓存标签
     * @param tagsList List格式的标签对象
     */
    void setTag(long weixinAppid, List<TagDTO> tagsList) throws JsonProcessingException;


}
