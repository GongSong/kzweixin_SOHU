package com.kuaizhan.manager;

import com.kuaizhan.config.WxApiConfig;
import com.kuaizhan.constant.WxErrCode;
import com.kuaizhan.exception.weixin.WxApiException;
import com.kuaizhan.exception.weixin.WxDataFormatException;
import com.kuaizhan.exception.weixin.WxInvalidTemplateException;
import com.kuaizhan.utils.HttpClientUtil;
import com.kuaizhan.utils.JsonUtil;
import org.json.JSONObject;

/**
 * 微信模板消息相关的接口封装
 * Created by zixiong on 2017/5/12.
 */
public class WxTplManager {

    /**
     * 发送模板消息
     * @throws WxApiException: 微信接口返回结果为null, 不能json序列化，或返回未知错误码.
     * @throws WxInvalidTemplateException: 无效的模板id或者用户不存在此模板
     * @throws WxDataFormatException: dataJson参数和模板定义的不一致
     * @return 微信服务器的消息id
     */
    public static long sendTplMsg(String accessToken, String openId, String templateId, String url, JSONObject dataJson) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("touser", openId);
        paramJson.put("template_id", templateId);
        paramJson.put("url", url);
        paramJson.put("data", dataJson);

        String paramStr = JsonUtil.bean2String(paramJson.toMap());
        String result = HttpClientUtil.postJson(WxApiConfig.getSendTplMsgUrl(accessToken), paramStr);

        if (result == null) {
            throw new WxApiException("[WeiXin:sendTplMsg] result is null");
        }

        JSONObject resultJson = new JSONObject();
        int errCode = resultJson.optInt("errcode");
        long msgId = resultJson.optLong("msgid");

        if (errCode == WxErrCode.INVALID_TEMPLATE_ID) {
            throw new WxInvalidTemplateException("[Weixin:sendTplMsg] openId:" + openId + " templateId:" + templateId);
        } else if (errCode == WxErrCode.DATA_FORMAT_ERROR) {
            throw new WxDataFormatException("[Weixin:sendTplMsg] openId:" + openId + " templateId:" + templateId + " dataStr:" + paramStr);
        } else if (errCode != 0 || msgId == 0) {
            throw new WxApiException("[Weixin:sendTplMsg] not expected result:" + resultJson + " openId:" + openId + " templateId:" + templateId + " dataStr:" + paramStr);
        }
        return msgId;
    }
}
