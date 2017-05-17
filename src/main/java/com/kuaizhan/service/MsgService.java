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
     * 获取未读消息数目
     */
    long getUnreadMsgCount(long weixinAppid);

    /**
     * 更新消息的最新阅读时间
     */
    void markMsgRead(long weixinAppid);

    /**
     * 分页查询公众号消息列表
     * @param queryStr 内容筛选条件
     * @param filterKeywords 是否过滤关键词回复
     * @param pageNum 页码
     * @return
     */
    Page<MsgPO> listMsgsByPagination(long weixinAppid, String queryStr, boolean filterKeywords, int pageNum);

    /**
     * 获取和单个用户的聊天列表
     *
     * @param openId 用户openId
     */
    Page<MsgPO> listMsgsByOpenId(long weixinAppid, String openId, int pageNum);

    /**
     * 获取用户的快速回复设置
     */
    List<String> getQuickReplies(long weixinAppid);

    /**
     * 更新快速回复设置, 全覆盖更新
     */
    void updateQuickReplies(long weixinAppid, List<String> replies);

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
