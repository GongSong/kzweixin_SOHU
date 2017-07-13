package com.kuaizhan.kzweixin.manager;

import com.kuaizhan.kzweixin.config.WxApiConfig;
import com.kuaizhan.kzweixin.constant.WxErrCode;
import com.kuaizhan.kzweixin.entity.msg.CustomMsg;
import com.kuaizhan.kzweixin.enums.MsgType;
import com.kuaizhan.kzweixin.exception.weixin.WxApiException;
import com.kuaizhan.kzweixin.exception.weixin.WxInvalidOpenIdException;
import com.kuaizhan.kzweixin.exception.weixin.WxOutOfResponseLimitException;
import com.kuaizhan.kzweixin.utils.HttpClientUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信消息管理相关接口封装
 * Created by zixiong on 2017/5/17.
 */
public class WxMsgManager {

    /**
     * 给用户发送客服消息
     * @param openId 用户的openId
     * @param customMsg 消息数据
     * @throws IllegalArgumentException 图文数据不合法
     * @throws WxOutOfResponseLimitException 回复次数超过限制
     */
    public static void sendCustomMsg(String accessToken, String openId, CustomMsg customMsg)
            throws IllegalArgumentException, WxOutOfResponseLimitException {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("touser", openId);

        MsgType msgType = customMsg.getMsgType();
        if (msgType == MsgType.TEXT) {
            paramMap.put("msgtype", "text");
            paramMap.put("text", customMsg.getText());
        } else if (msgType == MsgType.IMAGE) {
            paramMap.put("msgtype", "image");
            paramMap.put("image", customMsg.getImage());
        } else if (msgType == MsgType.NEWS) {
            paramMap.put("msgtype", "news");
            paramMap.put("news", customMsg.getNews());
        } else {
            throw new IllegalArgumentException("[Weixin:sendCustomMsg]: msgType not allowed:" + msgType);
        }

        String result = HttpClientUtil.postJson(WxApiConfig.sendCustomMsgUrl(accessToken), JsonUtil.bean2String(paramMap));

        if (result == null) {
            throw new WxApiException("[Weixin:sendCustomMsg] result is null");
        }

        JSONObject resultJson = new JSONObject(result);
        int errCode = resultJson.optInt("errcode");

        // 数据错误时，微信竟然回返回这个错误码，垃圾!!!
        if (errCode == WxErrCode.INVALID_OPEN_ID) {
            throw new WxInvalidOpenIdException("[Weixin:sendCustomMsg] invalid openId:" + openId);
        }
        if (errCode == WxErrCode.OUT_OF_RESPONSE_LIMIT) {
            throw new WxOutOfResponseLimitException();
        }
        if (errCode != 0) {
            throw new WxApiException("[Weixin:sendCustomMsg] unexpected result:" + resultJson +
                    " content:" + customMsg.getContentJsonStr());
        }
    }
}
