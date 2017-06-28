package com.kuaizhan.kzweixin.manager;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.config.WxApiConfig;
import com.kuaizhan.kzweixin.constant.WxErrCode;
import com.kuaizhan.kzweixin.exception.weixin.WxApiException;
import com.kuaizhan.kzweixin.exception.weixin.WxInvalidImageFormatException;
import com.kuaizhan.kzweixin.exception.weixin.WxMediaSizeOutOfLimitException;
import com.kuaizhan.kzweixin.utils.HttpClientUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import com.kuaizhan.kzweixin.utils.UrlUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zixiong on 2017/5/31.
 */
public class WxCommonManager {

    /**
     * 上传临时图片素材到微信, 换取临时mediaId
     * @throws WxMediaSizeOutOfLimitException 图片过大
     * @throws WxInvalidImageFormatException 不支持的图片类型
     */
    public static String uploadTmpImage(String accessToken, String imgUrl) throws WxInvalidImageFormatException, WxMediaSizeOutOfLimitException {

        imgUrl = UrlUtil.fixQuote(imgUrl);
        // 获取内部地址
        Map<String ,String> address = UrlUtil.getPicIntranetAddress(imgUrl);
        String result = HttpClientUtil.postFile(WxApiConfig.addTmpMedia(accessToken, "image"), address.get("url"), address.get("host"));

        if (result == null) {
            throw new WxApiException("[WeiXin:uploadTmpImage] result is null");
        }

        JSONObject returnJson = new JSONObject(result);
        int errCode = returnJson.optInt("errcode");
        String mediaId = returnJson.optString("media_id");

        if (errCode == WxErrCode.MEDIA_SIZE_OUT_OF_LIMIT) {
            throw new WxMediaSizeOutOfLimitException();
        } else if (errCode == WxErrCode.INVALID_IMAGE_FORMAT) {
            throw new WxInvalidImageFormatException();
        } else if (errCode != 0 || mediaId == null) {
            throw new WxApiException("[Weixin:uploadTmpImage] unexpected result, imgUrl:" + imgUrl + " result:" + result);
        }
        return mediaId;
    }

    /**
     * 获取临时二维码ticket
     * @param sceneId 场景id
     * @return
     */
    public static String genTmpQrcode(String accessToken, int sceneId) {
        Map<String, Object> param = new HashMap<>();
        // 临时二维码最大30天
        param.put("expire_seconds", 30 * 24 * 60 * 60);
        param.put("action_name", "QR_SCENE");
        param.put("action_info", ImmutableMap.of("scene", ImmutableMap.of("scene_id", sceneId)));

        String result = HttpClientUtil.postJson(WxApiConfig.getAddQrcodeUrl(accessToken), JsonUtil.bean2String(param));

        if (result == null) {
            throw new WxApiException("[Weixin:genTmpQrcode] result is null");
        }

        JSONObject resultJson = new JSONObject(result);
        int errCode = resultJson.optInt("errcode");
        String ticket =  resultJson.optString("ticket");

        if (errCode != 0 || ticket == null) {
            throw new WxApiException("[Weixin:genTmpQrcode] unexpected result:" + result);
        }

        return ticket;
    }
}
