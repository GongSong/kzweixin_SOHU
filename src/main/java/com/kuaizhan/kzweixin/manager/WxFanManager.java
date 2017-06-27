package com.kuaizhan.kzweixin.manager;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.config.WxApiConfig;
import com.kuaizhan.kzweixin.constant.WxErrCode;
import com.kuaizhan.kzweixin.entity.fan.TagDTO;
import com.kuaizhan.kzweixin.entity.fan.TagWrapper;
import com.kuaizhan.kzweixin.exception.weixin.*;
import com.kuaizhan.kzweixin.utils.HttpClientUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
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
            WxTagNumExceedException, WxApiException{
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

        JSONObject resultJson = new JSONObject(result);
        int errCode = resultJson.optInt("errcode");

        if (errCode == 0) {
            TagWrapper wrapper = JsonUtil.string2Bean(result, TagWrapper.class);
            return wrapper.getTags();
        } else {
            throw new WxApiException("[WeiXin:getTags] unexpected error, result:" + result);
        }
    }

    public static void updateTag(String accessToken, int tagId, String tagName) throws WxDuplicateTagException,
            WxTagReservedModifiedException, WxApiException {
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
        String result = HttpClientUtil.postJson(WxApiConfig.deleteTagUrl(accessToken), JsonUtil.bean2String(paramMap));

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

    public static void addFanTag(String accessToken, List<String> fansOpenId, int tagId) throws WxOpenIdExceedException,
            WxInvalidTagException, WxFansTagExceedException, WxInvalidOpenIdException, WxOpenIdMismatchException,
            WxApiException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("openid_list", fansOpenId);
        paramMap.put("tagid", tagId);
        String result = HttpClientUtil.postJson(WxApiConfig.setUserTagUrl(accessToken), JsonUtil.bean2String(paramMap));

        if (result == null) {
            throw new WxApiException("[WeiXin:addFanTag] result is null");
        }

        JSONObject resultJson = new JSONObject(result);
        int errCode = resultJson.optInt("errcode");

        if (errCode == 0) {
            return;
        }

        if (errCode == WxErrCode.OPEN_ID_EXCEED) {
            throw new WxOpenIdExceedException("[Weixin:addFanTag] tagId:" + tagId);
        } else if (errCode == WxErrCode.INVALID_TAG) {
            throw new WxInvalidTagException("[Weixin:addFanTag] tagId:" + tagId);
        } else if (errCode == WxErrCode.FANS_TAG_EXCEED) {
            throw new WxFansTagExceedException("[Weixin:addFanTag] tagId:" + tagId);
        } else if (errCode == WxErrCode.INVALID_OPEN_ID) {
            throw new WxInvalidOpenIdException("[Weixin:addFanTag] tagId:" + tagId);
        } else if (errCode == WxErrCode.OPEN_ID_MISMATCH_APPID) {
            throw new WxOpenIdMismatchException("[Weixin:addFanTag] tagId:" + tagId);
        } else {
            throw new WxApiException("[Weixin:addFanTag] not expected result:" + resultJson +  " tagId:" + tagId);
        }
    }

    public static void deleteFanTag(String accessToken, List<String> fansOpenId, int tagId) throws WxOpenIdExceedException,
            WxInvalidTagException, WxInvalidOpenIdException, WxOpenIdMismatchException, WxApiException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("openid_list", fansOpenId);
        paramMap.put("tagid", tagId);
        String result = HttpClientUtil.postJson(WxApiConfig.deleteUserTagUrl(accessToken), JsonUtil.bean2String(paramMap));

        if (result == null) {
            throw new WxApiException("[WeiXin:deleteFanTag] result is null");
        }

        JSONObject resultJson = new JSONObject(result);
        int errCode = resultJson.optInt("errcode");

        if (errCode == 0) {
            return;
        }

        if (errCode == WxErrCode.OPEN_ID_EXCEED) {
            throw new WxOpenIdExceedException("[Weixin:deleteFanTag] tagId:" + tagId);
        } else if (errCode == WxErrCode.INVALID_TAG) {
            throw new WxInvalidTagException("[Weixin:deleteFanTag] tagId:" + tagId);
        } else if (errCode == WxErrCode.INVALID_OPEN_ID) {
            throw new WxInvalidOpenIdException("[Weixin:deleteFanTag] tagId:" + tagId);
        } else if (errCode == WxErrCode.OPEN_ID_MISMATCH_APPID) {
            throw new WxOpenIdMismatchException("[Weixin:deleteFanTag] tagId:" + tagId);
        } else {
            throw new WxApiException("[Weixin:deleteFanTag] not expected result:" + resultJson +  " tagId:" + tagId);
        }
    }

    public static void addBlacklist(String accessToken, List<String> fansOpenId) throws WxInvalidOpenIdException,
            WxOpenIdMismatchException, WxBlacklistExceedException, WxApiException{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("openid_list", fansOpenId);
        String result = HttpClientUtil.postJson(WxApiConfig.insertBlackUrl(accessToken), JsonUtil.bean2String(paramMap));

        if (result == null) {
            throw new WxApiException("[WeiXin:addBlacklist] result is null");
        }

        JSONObject resultJson = new JSONObject(result);
        int errCode = resultJson.optInt("errcode");

        if (errCode == 0) {
            return;
        }

        if (errCode == WxErrCode.INVALID_OPEN_ID) {
            throw new WxInvalidOpenIdException("[Weixin:addBlacklist] fansOpenId:" + fansOpenId.toString());
        } else if (errCode == WxErrCode.OPEN_ID_MISMATCH_APPID) {
            throw new WxOpenIdMismatchException("[Weixin:addBlacklist] fansOpenId:" + fansOpenId.toString());
        } else if (errCode == WxErrCode.ADD_BLACKLIST_EXCEED_LIMIT) {
            throw new WxBlacklistExceedException("[Weixin:addBlacklist] fansOpenId:" + fansOpenId.toString());
        } else {
            throw new WxApiException("[Weixin:addBlacklist] not expected result:" + resultJson +  " fansOpenId:" + fansOpenId.toString());
        }
    }

    public static void removeBlacklist(String accessToken, List<String> fansOpenId) throws WxInvalidOpenIdException,
            WxOpenIdMismatchException, WxBlacklistExceedException, WxApiException{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("openid_list", fansOpenId);
        String result = HttpClientUtil.postJson(WxApiConfig.deleteBlackUrl(accessToken), JsonUtil.bean2String(paramMap));

        if (result == null) {
            throw new WxApiException("[WeiXin:removeBlacklist] result is null");
        }

        JSONObject resultJson = new JSONObject(result);
        int errCode = resultJson.optInt("errcode");

        if (errCode == 0) {
            return;
        }

        if (errCode == WxErrCode.INVALID_OPEN_ID) {
            throw new WxInvalidOpenIdException("[Weixin:removeBlacklist] fansOpenId:" + fansOpenId.toString());
        } else if (errCode == WxErrCode.OPEN_ID_MISMATCH_APPID) {
            throw new WxOpenIdMismatchException("[Weixin:removeBlacklist] fansOpenId:" + fansOpenId.toString());
        } else if (errCode == WxErrCode.ADD_BLACKLIST_EXCEED_LIMIT) {
            throw new WxBlacklistExceedException("[Weixin:removeBlacklist] fansOpenId:" + fansOpenId.toString());
        } else {
            throw new WxApiException("[Weixin:removeBlacklist] not expected result:" + resultJson +  " fansOpenId:" + fansOpenId.toString());
        }
    }
}
