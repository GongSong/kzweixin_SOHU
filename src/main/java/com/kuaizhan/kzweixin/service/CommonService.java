package com.kuaizhan.kzweixin.service;

import com.kuaizhan.kzweixin.entity.responsejson.ResponseJson;
import com.kuaizhan.kzweixin.enums.ComponentResponseType;
import com.kuaizhan.kzweixin.enums.MsgType;

import java.util.Map;

/**
 * 公共service
 * Created by zixiong on 2017/6/26.
 */
public interface CommonService {
    /**
     * 统计+1
     * @param traceId 业务id
     * @param traceKey 业务key
     */
    void kzStat(String traceId, String traceKey);

    /**
     * 把前端传来的responseJson格式化
     * @param weixinAppid weixinAppid PostResponseJson需要
     * @param responseJsonMap json数据map
     * @param responseType 类型
     */
    ResponseJson getResponseJsonFromParam(long weixinAppid, Map responseJsonMap, ComponentResponseType responseType);

    /**
     * 把数据库查出来的responseJson格式化
     */
    ResponseJson getResponseJsonFromDB(String responseJson, ComponentResponseType responseType);


    /**
     * 客服消息模块，把ResponseJson格式化
     */
    ResponseJson getMsgResponseJsonFromParam(long weixinAppid, Map responseJsonMap, MsgType msgType);


    /**
     * 把数据库查出来的responseJson格式化
     */
    ResponseJson getMsgResponseJsonFromDB(String responseJson, MsgType msgType);
}
