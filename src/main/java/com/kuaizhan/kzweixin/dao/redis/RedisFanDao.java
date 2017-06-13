package com.kuaizhan.kzweixin.dao.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuaizhan.kzweixin.pojo.po.FanPO;
import com.kuaizhan.kzweixin.pojo.dto.TagDTO;

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
     * 清除tag
     *
     * @param siteId
     */
    void deleteTag(long siteId);
}
