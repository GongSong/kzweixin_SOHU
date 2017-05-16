package com.kuaizhan.service;


import com.kuaizhan.exception.deprecated.business.SendCustomMsgException;
import com.kuaizhan.exception.common.DaoException;
import com.kuaizhan.exception.common.RedisException;
import com.kuaizhan.pojo.po.AccountPO;
import com.kuaizhan.pojo.po.MsgPO;
import com.kuaizhan.pojo.dto.Page;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;


/**
 * 消息管理服务接口
 * Created by Mr.Jadyn on 2017/1/19.
 */
public interface MsgService {
    /**
     * 获取数量
     *
     * @param appId   公众号appid
     * @param status  0删除1未读2已读3已回复
     * @param keyword 关键词
     * @param isHide  是否隐藏关键词查询
     * @return
     */
    long countMsg(String appId, int status, int sendType, String keyword, int isHide) throws DaoException;

    /**
     * 分页查询
     *
     * @param appId   公众号appId
     * @param page    页码
     * @param keyword 搜索关键词
     * @param isHide  是否为隐藏关键词查询
     * @return
     * @throws IOException
     */
    Page<MsgPO> listMsgsByPagination(long siteId, String appId, int page, String keyword, int isHide) throws DaoException, RedisException;


    /**
     * 获取新消息列表
     *
     * @param appId 公众号appId
     * @return
     */
    List<MsgPO> listNewMsgs(String appId) throws DaoException;


    /**
     * 获取单个用户的消息列表
     *
     * @param appId  公众号appId
     * @param openId 用户openId
     * @return
     */
    Page<MsgPO> listMsgsByOpenId(long siteId, String appId, String openId, int page) throws RedisException, DaoException;

    /**
     * 批量更新消息状态
     *
     * @param appId 公众号appid
     * @param msgs  消息列表
     */
    void updateMsgsStatus(long siteId, String appId, List<MsgPO> msgs) throws DaoException, RedisException;

    /**
     * 插入消息
     *
     * @param siteId
     * @param appId
     * @param msgPO
     */
    void insertMsg(long siteId, String appId, MsgPO msgPO) throws DaoException, RedisException;


    /**
     * 插入客服消息
     *
     */
    void insertCustomMsg(AccountPO accountPO, String openId, int msgType, JSONObject content) throws SendCustomMsgException, DaoException, RedisException;

}
