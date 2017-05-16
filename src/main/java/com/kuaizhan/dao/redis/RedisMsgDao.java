package com.kuaizhan.dao.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuaizhan.pojo.po.MsgPO;

import java.io.IOException;
import java.util.List;

/**
 * Created by liangjiateng on 2017/3/18.
 */
public interface RedisMsgDao {
    /**
     * 获取消息列表
     *
     * @param siteId
     * @param field
     * @return
     */
    List<MsgPO> listMsgsByPagination(long siteId, String field) throws IOException;


    /**
     * 设置缓存
     *
     * @param siteId
     * @param field
     */
    void setMsgsByPagination(long siteId, String field, List<MsgPO> msgs) throws JsonProcessingException;

    /**
     * 删除消息列表缓存
     *
     * @param siteId
     */
    void deleteMsgsByPagination(long siteId);

    List<MsgPO> listMsgsByOpenId(long siteId, String openId, int page) throws IOException;

    void setMsgsByOpenId(long siteId, String openId, int page, List<MsgPO> msgs) throws JsonProcessingException;

    void deleteMsgsByOpenId(long siteId, String openId);

}
