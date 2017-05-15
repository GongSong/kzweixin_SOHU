package com.kuaizhan.service;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by zixiong on 2017/5/13.
 */
public interface TplService {

    /**
     * 给公众号添加模板, 幂等操作
     * @param tplIdShort 模板的编号(简称)
     */
    void addTpl(long weixinAppid, String tplIdShort);

    /**
     * 给指定用户发送系统模板消息
     * @param tplIdShort 模板编号, 只能传系统模板
     * @param openId 用户的openId
     * @param url 模板消息的跳转链接
     * @param dataMap 模板消息的内容
     */
    void sendSysTplMsg(long weixinAppid, String tplIdShort, String openId, String url, Map dataMap);

    /**
     * 给指定用户发送模板消息
     * @param tplId 模板的id, 每个公众号都不一样
     * @param openId 用户的openId
     * @param url 模板消息的跳转链接
     * @param dataMap 模板消息的内容
     */
    void sendTplMsg(long weixinAppid, String tplId, String openId, String url, Map dataMap);

    /**
     * 判断公众号是否添加某模板
     * 如果用户在公众后台删除模板，此状态只有在某一次发送消息失败时才会更新
     */
    boolean isTplAdded(long weixinAppid, String tplIdShort);
}
