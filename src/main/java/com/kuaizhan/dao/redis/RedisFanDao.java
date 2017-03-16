package com.kuaizhan.dao.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuaizhan.pojo.DO.FanDO;
import com.kuaizhan.pojo.DTO.TagDTO;

import java.io.IOException;
import java.util.List;


/**
 * Created by liangjiateng on 2017/3/16.
 */
public interface RedisFanDao {
    /**
     * 分页查询粉丝列表
     *
     * @return
     */
    List<FanDO> listFanByPagination(long siteId, String field) throws IOException;

    /**
     * 缓存分页查询数据
     *
     * @param siteId
     * @param field
     * @param fanses
     */
    void setFanByPagination(long siteId, String field, List<FanDO> fanses) throws JsonProcessingException;

    /**
     * 存在分页缓存
     *
     * @param siteId
     * @param field
     * @return
     */
    boolean existFanByPagination(long siteId, String field);

    /**
     * 删除分页缓存
     * @param siteId
     */
    void deleteFanByPagination(long siteId);

    /**
     * 获取所有标签
     *
     * @return
     */
    List<TagDTO> listTags(long siteId) throws IOException;

    /**
     * 缓存标签
     *
     * @param tags
     */
    void setTag(long siteId, List<TagDTO> tags) throws JsonProcessingException;

    /**
     * 判断存在tag
     *
     * @param siteId
     * @return
     */
    boolean existTag(long siteId);

    /**
     * 清除tag
     *
     * @param siteId
     */
    void deleteTag(long siteId);
}
