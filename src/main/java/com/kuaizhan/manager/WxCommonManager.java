package com.kuaizhan.manager;

import com.kuaizhan.config.WxApiConfig;
import com.kuaizhan.constant.WxErrCode;
import com.kuaizhan.exception.weixin.WxApiException;
import com.kuaizhan.exception.weixin.WxInvalidImageFormat;
import com.kuaizhan.exception.weixin.WxMediaSizeOutOfLimit;
import com.kuaizhan.utils.HttpClientUtil;
import com.kuaizhan.utils.UrlUtil;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by zixiong on 2017/5/31.
 */
public class WxCommonManager {

    /**
     * 上传临时图片素材到微信, 换取临时mediaId
     */
    public static String uploadTmpImage(String accessToken, String imgUrl) {

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
            throw new WxMediaSizeOutOfLimit();
        } else if (errCode == WxErrCode.INVALID_IMAGE_FORMAT) {
            throw new WxInvalidImageFormat();
        } else if (errCode != 0 || mediaId == null) {
            throw new WxApiException("[Weixin:uploadTmpImage] not expected result, imgUrl:" + imgUrl + " result:" + result);
        }
        return mediaId;
    }
}
