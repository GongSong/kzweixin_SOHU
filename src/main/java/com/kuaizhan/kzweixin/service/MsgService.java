package com.kuaizhan.kzweixin.service;


import com.kuaizhan.kzweixin.entity.common.PageV2;
import com.kuaizhan.kzweixin.entity.msg.CustomMsg;
import com.kuaizhan.kzweixin.enums.MsgType;
import com.kuaizhan.kzweixin.dao.po.MsgPO;
import com.kuaizhan.kzweixin.entity.common.Page;

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
     */
    PageV2<MsgPO> listMsgsByPage(long weixinAppid, String queryStr, boolean filterKeywords, int offset, int limit);

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
     * 发送客服消息
     * @param weixinAppid 微信appId
     * @param openId 用户openId
     * @param customMsg 消息数据
     * @throws IllegalArgumentException 客服消息数据有误
     */
    void sendCustomMsg(long weixinAppid, String openId, CustomMsg customMsg);

    /**
     * 获取快站推送token
     */
    String getPushToken(long weixinAppid, String openId);

    /**
     * 删除推送token
     */
    void deletePushToken(long weixinAppid, String openId);
}
