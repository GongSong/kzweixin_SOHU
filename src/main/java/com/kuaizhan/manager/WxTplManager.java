package com.kuaizhan.manager;

import com.kuaizhan.config.WxApiConfig;
import com.kuaizhan.constant.WxErrCode;
import com.kuaizhan.exception.weixin.*;
import com.kuaizhan.utils.HttpClientUtil;
import com.kuaizhan.utils.JsonUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信模板消息相关的接口封装
 * Created by zixiong on 2017/5/12.
 */
public class WxTplManager {

    /**
     * 发送模板消息
     * @param tplId 用户的模板消息id
     * @param openId 发送对象
     * @param dataMap 模板消息data数据
     * @throws WxApiException: 微信接口返回结果为null, 不能json序列化，或返回未知错误码
     * @throws WxInvalidTemplateException: 无效的模板id或者用户不存在此模板
     * @throws WxInvalidOpenIdException: 无效的openId
     * @throws WxDataFormatException: dataJson参数和模板定义的不一致
     * @return 微信服务器的消息id
     */
    public static long sendTplMsg(String accessToken, String tplId, String openId, String url, Map dataMap)
            throws WxInvalidTemplateException, WxInvalidOpenIdException, WxDataFormatException, WxApiException {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("touser", openId);
        paramMap.put("template_id", tplId);
        paramMap.put("url", url);
        paramMap.put("data", dataMap);

        String paramStr = JsonUtil.bean2String(paramMap);
        String result = HttpClientUtil.postJson(WxApiConfig.getSendTplMsgUrl(accessToken), paramStr);

        if (result == null) {
            throw new WxApiException("[WeiXin:sendTplMsg] result is null");
        }

        JSONObject resultJson = new JSONObject(result);
        int errCode = resultJson.optInt("errcode");
        long msgId = resultJson.optLong("msgid");

        if (errCode == WxErrCode.INVALID_TEMPLATE_ID) {
            throw new WxInvalidTemplateException("[Weixin:sendTplMsg] openId:" + openId + " tplId:" + tplId);
        } else if (errCode == WxErrCode.INVALID_OPEN_ID) {
            throw new WxInvalidOpenIdException("[Weixin:sendTplMsg] openId:" + openId + " tplId");
        } else if (errCode == WxErrCode.DATA_FORMAT_ERROR) {
            throw new WxDataFormatException("[Weixin:sendTplMsg] openId:" + openId + " tplId:" + tplId + " dataStr:" + paramStr);
        } else if (errCode != 0 || msgId == 0) {
            throw new WxApiException("[Weixin:sendTplMsg] not expected result:" + resultJson + " openId:" + openId + " tplId:" + tplId + " dataStr:" + paramStr);
        }
        return msgId;
    }

    /**
     * 添加模板消息接口封装
     * @param tplIdShort 要添加的模板消息代码
     * @return 用户的版版消息id
     * @throws WxApiException 未知错误
     * @throws WxTemplateNumExceedException 公众号的模板数已经达到上限
     * @throws WxTemplateIndustryConflictException 微信的行业设置与模板id冲突
     */
    public static String addTplId(String accessToken, String tplIdShort) throws WxTemplateNumExceedException, WxApiException {
        JSONObject paramJson = new JSONObject();
        paramJson.put("template_id_short", tplIdShort);

        String result = HttpClientUtil.postJson(WxApiConfig.getAddTemplateUrl(accessToken), paramJson.toString());

        if (result == null) {
            throw new WxApiException("[WeiXin:addTplId] result is null");
        }

        JSONObject resultJson = new JSONObject(result);
        int errCode = resultJson.optInt("errcode");
        String tplId = resultJson.optString("template_id", null);
        if (errCode == WxErrCode.TEMPLATE_NUM_EXCEEDS_LIMIT) {
            throw new WxTemplateNumExceedException();
        } else if (errCode == WxErrCode.TEMPLATE_INDUSTRY_CONFLICT) {
            throw new WxTemplateIndustryConflictException();
        } else if (errCode != 0 || tplId == null) {
            throw new WxApiException("[Weixin:addTplId] not expected result:" + resultJson + " tplIdShort:" + tplIdShort);
        }
        return tplId;
    }
}
