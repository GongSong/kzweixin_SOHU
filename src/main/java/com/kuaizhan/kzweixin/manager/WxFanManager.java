package com.kuaizhan.kzweixin.manager;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.config.WxApiConfig;
import com.kuaizhan.kzweixin.constant.WxErrCode;
import com.kuaizhan.kzweixin.entity.fan.TagDTO;
import com.kuaizhan.kzweixin.entity.fan.TagWrapper;
import com.kuaizhan.kzweixin.exception.weixin.WxApiException;
import com.kuaizhan.kzweixin.exception.weixin.WxDuplicateTagException;
import com.kuaizhan.kzweixin.exception.weixin.WxTagLengthExceedException;
import com.kuaizhan.kzweixin.exception.weixin.WxTagNumExceedException;
import com.kuaizhan.kzweixin.exception.weixin.WxTagReservedModifiedException;
import com.kuaizhan.kzweixin.exception.weixin.WxFansNumExceedException;
import com.kuaizhan.kzweixin.utils.HttpClientUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信粉丝管理模块相关接口封装
 * Created by fangtianyu on 6/15/17.
 */
public class WxFanManager {
    public static int createTag(String accessToken, String tagName) throws WxDuplicateTagException,
            WxTagLengthExceedException, WxTagNumExceedException, WxApiException{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tag", ImmutableMap.of("name", tagName));
        String paramStr = JsonUtil.bean2String(paramMap);

        String result = HttpClientUtil.postJson(WxApiConfig.getCreateTagsUrl(accessToken), paramStr);

        if (result == null) {
            throw new WxApiException("[WeiXin:createNewTag] result is null");
        }

        JSONObject resultJson = new JSONObject(result);
        JSONObject resultTagJson = resultJson.optJSONObject("tag");
        if (resultTagJson != null) {
            return resultTagJson.getInt("id");
        }

        int errCode = resultJson.optInt("errcode");
        if (errCode == WxErrCode.DUPLICATED_TAGS) {
            throw new WxDuplicateTagException("[Weixin:createNewTag] tagName:" + tagName);
        } else if (errCode == WxErrCode.TAG_LENGTH_EXCEEDS) {
            throw new WxTagLengthExceedException("[Weixin:createNewTag] tagName:" + tagName);
        } else if (errCode == WxErrCode.TAG_NUM_EXCEEDS) {
            throw new WxTagNumExceedException("[Weixin:createNewTag] tagName:" + tagName);
        } else {
            throw new WxApiException("[Weixin:createNewTag] not expected result:" + resultJson + " tagName:" + tagName);
        }
    }

    public static List<TagDTO> getTags(String accessToken) throws WxApiException{
        String result = HttpClientUtil.get(WxApiConfig.getTagsUrl(accessToken));
        if (result == null) {
            throw new WxApiException("[WeiXin:getTags] result is null");
        }

        TagWrapper wrapper = JsonUtil.string2Bean(result, TagWrapper.class);
        if (wrapper.getTags() == null || wrapper.getTags().size() == 0) {
            throw new WxApiException("[WeiXin:getTags] unexpected error, result:" + result);
        }
        return wrapper.getTags();
    }

    public static void updateTag(String accessToken, int tagId, String tagName) throws WxDuplicateTagException,
            WxTagLengthExceedException, WxTagReservedModifiedException, WxApiException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tag", ImmutableMap.of("id", tagId, "name", tagName));
        String result = HttpClientUtil.postJson(WxApiConfig.updateTagsUrl(accessToken), JsonUtil.bean2String(paramMap));

        if (result == null) {
            throw new WxApiException("[WeiXin:updateTag] result is null");
        }

        JSONObject resultJson = new JSONObject(result);
        int errCode = resultJson.optInt("errcode");

        if (errCode == 0) {
            return;
        }

        if (errCode == WxErrCode.DUPLICATED_TAGS) {
            throw new WxDuplicateTagException("[Weixin:updateTag] tagId:" + tagId + " tagName:" + tagName);
        } else if (errCode == WxErrCode.TAG_LENGTH_EXCEEDS) {
            throw new WxTagLengthExceedException("[Weixin:updateTag] tagId:" + tagId + " tagName:" + tagName);
        } else if (errCode == WxErrCode.TAG_RESERVED_MODIFIED) {
            throw new WxTagReservedModifiedException("[Weixin:updateTag] tagId:" + tagId + " tagName:" + tagName);
        } else {
            throw new WxApiException("[Weixin:updateTag] not expected result:" + resultJson +  " tagId:" + tagId + " tagName:" + tagName);
        }
    }

    public static void deleteTag(String accessToken, int tagId) throws WxTagReservedModifiedException,
            WxFansNumExceedException, WxApiException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tag", ImmutableMap.of("id", tagId));
        String result = HttpClientUtil.postJson(WxApiConfig.deleteTagsUrl(accessToken), JsonUtil.bean2String(paramMap));

        if (result == null) {
            throw new WxApiException("[WeiXin:deleteTag] result is null");
        }

        JSONObject resultJson = new JSONObject(result);
        int errCode = resultJson.optInt("errcode");

        if (errCode == 0) {
            return;
        }

        if (errCode == WxErrCode.TAG_RESERVED_MODIFIED) {
            throw new WxTagReservedModifiedException("[Weixin:deleteTag] tagId:" + tagId);
        } else if (errCode == WxErrCode.TAG_FANS_EXCEED_10W) {
            throw new WxFansNumExceedException("[Weixin:deleteTag] tagId:" + tagId);
        } else {
            throw new WxApiException("[Weixin:deleteTag] not expected result:" + resultJson +  " tagId:" + tagId);
        }
    }



}
