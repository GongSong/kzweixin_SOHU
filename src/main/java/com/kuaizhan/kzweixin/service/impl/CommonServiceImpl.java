package com.kuaizhan.kzweixin.service.impl;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.constant.KzExchange;
import com.kuaizhan.kzweixin.entity.responsejson.*;
import com.kuaizhan.kzweixin.enums.ComponentResponseType;
import com.kuaizhan.kzweixin.enums.MsgType;
import com.kuaizhan.kzweixin.service.CommonService;
import com.kuaizhan.kzweixin.service.PostService;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import com.kuaizhan.kzweixin.utils.MqUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by zixiong on 2017/6/26.
 */
@Service
public class CommonServiceImpl implements CommonService {
    @Resource
    private MqUtil mqUtil;
    @Resource
    private PostService postService;

    @Override
    public void kzStat(String traceId, String traceKey) {
        String msg = JsonUtil.bean2String(ImmutableMap.of(
                "trace_id", traceId,
                "trace_key", traceKey
        ));
        mqUtil.publish(KzExchange.KZ_STAT_INC, "", msg);
    }

    @Override
    public ResponseJson getResponseJsonFromParam(long weixinAppid, Map responseJsonMap, ComponentResponseType responseType) {
        String responseJsonStr = JsonUtil.bean2String(responseJsonMap);
        switch (responseType) {
            case TEXT:
                return JsonUtil.string2Bean(responseJsonStr, TextResponseJson.class);
            case IMAGE:
                return JsonUtil.string2Bean(responseJsonStr, ImageResponseJson.class);
            case POST:
                PostResponseJson postResponseJson = JsonUtil.string2Bean(responseJsonStr, PostResponseJson.class);
                return postService.getPostResponseJson(weixinAppid, postResponseJson.getMediaIds());
            case LINK_GROUP:
                return JsonUtil.string2Bean(responseJsonStr, LinkGroupResponseJson.class);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public ResponseJson getResponseJsonFromDB(String responseJsonStr, ComponentResponseType responseType) {
        ResponseJson responseJson;
        switch (responseType) {
            case TEXT:
                responseJson = JsonUtil.string2Bean(responseJsonStr, TextResponseJson.class);
                break;
            case IMAGE:
                responseJson = JsonUtil.string2Bean(responseJsonStr, ImageResponseJson.class);
                break;
            case LINK_GROUP:
                responseJson = JsonUtil.string2Bean(responseJsonStr, LinkGroupResponseJson.class);
                break;
            case POST:
                responseJson = JsonUtil.string2Bean(responseJsonStr, PostResponseJson.class);
                break;
            default:
                // TODO: 异常处理
                return null;
        }
        // 数据清洗
        responseJson.cleanAfterSelect();
        return responseJson;
    }

    @Override
    public ResponseJson getMsgResponseJsonFromParam(long weixinAppid, Map responseJsonMap, MsgType msgType) {
        String responseJsonStr = JsonUtil.bean2String(responseJsonMap);
        switch (msgType) {
            case POST:
                PostResponseJson postResponseJson = JsonUtil.string2Bean(responseJsonStr, PostResponseJson.class);
                return postService.getPostResponseJson(weixinAppid, postResponseJson.getMediaIds());
            case LINK_GROUP:
                return JsonUtil.string2Bean(responseJsonStr, LinkGroupResponseJson.class);
            case TEXT:
                return JsonUtil.string2Bean(responseJsonStr, TextResponseJson.class);
            case IMAGE:
                return JsonUtil.string2Bean(responseJsonStr, ImageResponseJson.class);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public ResponseJson getMsgResponseJsonFromDB(String responseJsonStr, MsgType msgType) {
        ResponseJson responseJson;
        switch (msgType) {
            case TEXT:
                responseJson = JsonUtil.string2Bean(responseJsonStr, TextResponseJson.class);
                break;
            case KEYWORD_TEXT:
                responseJson = JsonUtil.string2Bean(responseJsonStr, TextResponseJson.class);
                break;
            case IMAGE:
                responseJson = JsonUtil.string2Bean(responseJsonStr, ImageResponseJson.class);
                break;
            case LINK_GROUP:
                responseJson = JsonUtil.string2Bean(responseJsonStr, LinkGroupResponseJson.class);
                break;
            default:
                // TODO: 异常处理
                return null;
        }
        // 数据清洗
        responseJson.cleanAfterSelect();
        return responseJson;
    }
}
