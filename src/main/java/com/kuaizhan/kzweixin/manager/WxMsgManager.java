package com.kuaizhan.kzweixin.manager;

import com.kuaizhan.kzweixin.config.WxApiConfig;
import com.kuaizhan.kzweixin.constant.WxErrCode;
import com.kuaizhan.kzweixin.constant.WxMsgType;
import com.kuaizhan.kzweixin.exception.weixin.WxApiException;
import com.kuaizhan.kzweixin.exception.weixin.WxInvalidOpenIdException;
import com.kuaizhan.kzweixin.exception.weixin.WxOutOfResponseLimitException;
import com.kuaizhan.kzweixin.utils.HttpClientUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
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
     * @param msgType 消息类型
     * @param content 消息数据
     */
    public static void sendCustomMsg(String accessToken, String openId, WxMsgType msgType, Object content) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("touser", openId);
        paramMap.put("msgtype", msgType.getValue());
        paramMap.put(msgType.getValue(), content);

        String paramStr = JsonUtil.bean2String(paramMap);
        String result = HttpClientUtil.postJson(WxApiConfig.sendCustomMsgUrl(accessToken), paramStr);

        if (result == null) {
            throw new WxApiException("[WeiXin:sendCustomMsg] result is null");
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
            throw new WxApiException("[Weixin:sendCustomMsg] unexpected result:" + resultJson + " paramStr:" + paramStr);
        }
    }
}
