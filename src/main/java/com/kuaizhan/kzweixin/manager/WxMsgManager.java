package com.kuaizhan.kzweixin.manager;

import com.kuaizhan.kzweixin.config.WxApiConfig;
import com.kuaizhan.kzweixin.constant.UnirestConstant;
import com.kuaizhan.kzweixin.constant.WxErrCode;
import com.kuaizhan.kzweixin.entity.msg.MsgLinkGroupResponseJson;
import com.kuaizhan.kzweixin.entity.responsejson.ImageResponseJson;
import com.kuaizhan.kzweixin.entity.responsejson.ResponseJson;
import com.kuaizhan.kzweixin.entity.responsejson.TextResponseJson;
import com.kuaizhan.kzweixin.entity.http.param.CustomMsgParam;
import com.kuaizhan.kzweixin.entity.http.response.CustomMsgResponse;
import com.kuaizhan.kzweixin.exception.weixin.WxApiException;
import com.kuaizhan.kzweixin.exception.weixin.WxInvalidOpenIdException;
import com.kuaizhan.kzweixin.exception.weixin.WxOutOfResponseLimitException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.kuaizhan.kzweixin.entity.msg.MassMsg;
import com.kuaizhan.kzweixin.enums.WxMsgType;
import com.kuaizhan.kzweixin.utils.HttpClientUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信消息管理相关接口封装
 * Created by zixiong on 2017/5/17.
 */
public class WxMsgManager {

    private static Logger logger = LoggerFactory.getLogger(WxMsgManager.class);
    /**
     * 给用户发送客服消息
     * @param openId 用户的openId
     * @param responseJson 消息数据
     * @throws IllegalArgumentException 图文数据不合法
     * @throws WxOutOfResponseLimitException 回复次数超过限制
     */
    public static void sendCustomMsg(String accessToken, String openId, ResponseJson responseJson)
            throws IllegalArgumentException, WxOutOfResponseLimitException {

        CustomMsgParam customMsgParam = convertToCustomMsg(responseJson);
        customMsgParam.setTouser(openId);

        HttpResponse<CustomMsgResponse> httpResponse;
        try {
            httpResponse = Unirest
                    .post(WxApiConfig.sendCustomMsgUrl(accessToken))
                    .header(UnirestConstant.CONTENT_TYPE, UnirestConstant.JSON_CONTENT)
                    .body(customMsgParam)
                    .asObject(CustomMsgResponse.class);
        } catch (UnirestException e) {
            throw new WxApiException("[Weixin:sendCustomMsg] UnirestException", e);
        }
        CustomMsgResponse response = httpResponse.getBody();

        System.out.println("---->" + response);
        int errCode = response.getErrcode();
        // TODO: errcode=45015, errmsg=response out of time limit or subscription is canceled
        // 数据错误时，微信竟然回返回这个错误码，垃圾!!!
        if (errCode == WxErrCode.INVALID_OPEN_ID) {
            throw new WxInvalidOpenIdException("[Weixin:sendCustomMsg] invalid openId:" + openId);
        }
        if (errCode == WxErrCode.OUT_OF_RESPONSE_LIMIT) {
            throw new WxOutOfResponseLimitException();
        }
        if (errCode != 0) {
            throw new WxApiException("[Weixin:sendCustomMsg] unexpected result:" + response +
                    " content:" + responseJson);
        }
    }

    /**
     * 把客服消息的ResponseJson转换为customMsgParam
     */
    private static CustomMsgParam convertToCustomMsg(ResponseJson responseJson) {
        CustomMsgParam customMsg = new CustomMsgParam();

        if (responseJson instanceof TextResponseJson) {
            TextResponseJson textResponseJson = (TextResponseJson) responseJson;
            customMsg.setMsgType("text");
            customMsg.setText(new CustomMsgParam.Text(textResponseJson.getContent()));
        } else if (responseJson instanceof ImageResponseJson) {
            ImageResponseJson imageResponseJson = (ImageResponseJson) responseJson;
            customMsg.setMsgType("image");
            customMsg.setImage(new CustomMsgParam.Image(imageResponseJson.getMediaId()));
        } else if (responseJson instanceof MsgLinkGroupResponseJson) {
            MsgLinkGroupResponseJson linkGroupResponseJson = (MsgLinkGroupResponseJson) responseJson;
            customMsg.setMsgType("news");
            List<CustomMsgParam.Article> articles = new ArrayList<>();
            for (MsgLinkGroupResponseJson.LinkGroup linkGroup : linkGroupResponseJson.getLinkGroups()) {
                CustomMsgParam.Article article = new CustomMsgParam.Article();
                article.setTitle(linkGroup.getTitle());
                article.setDescription(linkGroup.getDescription());
                article.setPicUrl(linkGroup.getPicUrl());
                article.setUrl(linkGroup.getUrl());
                articles.add(article);
            }
            customMsg.setNews(new CustomMsgParam.News(articles));
        } else {
            throw new IllegalArgumentException("[convertToCustomMsg] unsupported responseJson: " + responseJson);
        }
        return customMsg;
    }

    public static String sendMassMsg(String accessToken, int tagId, WxMsgType msgType, Object contentObj) {

        Map<String, Object> paramMap = new HashMap<>();

        if(tagId == 0) {
            paramMap.put("filter", new MassMsg.Filter(true, 0));
        } else {
            paramMap.put("filter", new MassMsg.Filter(false, tagId));
        }

        paramMap.put(msgType.getValue(), contentObj);
        paramMap.put("msgtype", msgType.getValue());

        if(msgType == WxMsgType.MP_NEWS) {
            paramMap.put("send_ignore_reprint", 0);
        }

        String paramStr = JsonUtil.bean2String(paramMap);
        String result = HttpClientUtil.postJson(WxApiConfig.sendMassMsgUrl(accessToken), paramStr);

        if (result == null) {
            throw new WxApiException("[WeiXin:sendMassMsg] result is null");
        }

        JSONObject resultJson = new JSONObject(result);
        int errCode = resultJson.optInt("errcode");
        String errMsg = resultJson.optString("errmsg");
        String msgId = resultJson.optString("msg_id");

        if (errCode != 0) {
            logger.warn("[Weixin:sendMassMsg] unexpected result:" + resultJson + " paramStr:" + paramStr + " errCode:" + errCode + " errMsg:" + errMsg);
            return null;
        }

        return msgId;
    }
}

