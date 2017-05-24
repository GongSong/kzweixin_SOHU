package com.kuaizhan.manager;


import com.kuaizhan.config.WxApiConfig;
import com.kuaizhan.constant.ErrorCode;
import com.kuaizhan.constant.WxErrCode;
import com.kuaizhan.exception.BusinessException;
import com.kuaizhan.exception.common.*;
import com.kuaizhan.exception.weixin.WxMediaIdNotExistException;
import com.kuaizhan.exception.weixin.WxPostListGetException;
import com.kuaizhan.pojo.po.PostPO;
import com.kuaizhan.pojo.dto.WxPostListDTO;
import com.kuaizhan.pojo.dto.WxPostDTO;
import com.kuaizhan.utils.HttpClientUtil;
import com.kuaizhan.utils.JsonUtil;
import com.kuaizhan.utils.UrlUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * 微信图文模块接口封装
 * Created by liangjiateng on 2017/3/28.
 */
public class WxPostManager {

    private static Logger logger = LoggerFactory.getLogger(WxPostManager.class);

    /**
     * 微信删除图文
     */
    public static void deletePost(String mediaId, String accessToken) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("media_id", mediaId);

        String result = HttpClientUtil.postJson(WxApiConfig.deleteMaterialUrl(accessToken), jsonObject.toString());
        if (result == null) {
            logger.error("[WxPostManager.deletePost] 删除图文返回为null, mediaId:{}", mediaId);
            throw new BusinessException(ErrorCode.OPERATION_FAILED, "删除图文失败, 请稍后重试");
        }

        JSONObject returnJson = new JSONObject(result);
        int errCode = returnJson.optInt("errcode");
        if (errCode != 0) {
            // post被使用而不能被删除
            if (errCode == 48005) {
                throw new BusinessException(ErrorCode.POST_USED_BY_OTHER_ERROR);
            }
            // 40007错误码是已经被删除，不报错
            else if (errCode == 40007) return;
            else {
                // 未知错误
                logger.error("[WxPostManager.deletePost] 删除多图文失败, mediaId:{} result: {}", mediaId, returnJson);
                throw new BusinessException(ErrorCode.OPERATION_FAILED, "删除图文失败, 请稍后重试");
            }
        }
    }

    /**
     * 上传图片到微信永久素材
     * @throws DownloadFileFailedException 下载文件失败
     */
    public static HashMap<String, String> uploadImage(String accessToken, String imgUrl) throws DownloadFileFailedException {
        // 处理没有http头的问题

        imgUrl = UrlUtil.fixQuote(imgUrl);
        imgUrl = UrlUtil.fixProtocol(imgUrl);

        // 获取内部地址
        Map<String ,String> address = UrlUtil.getPicIntranetAddress(imgUrl);
        String result = HttpClientUtil.postFile(WxApiConfig.addMaterialUrl(accessToken, "image"), address.get("url"), address.get("host"));

        JSONObject returnJson = new JSONObject(result);
        int errCode = returnJson.optInt("errcode");
        if (errCode != 0) {
            if (errCode == WxErrCode.MEDIA_SIZE_OUT_OF_LIMIT) {
                // TODO: 被controller直接调用时合理，被其他service调用时不合理
                // TODO: 需要给用户压缩图片吗
                throw new BusinessException(ErrorCode.MEDIA_SIZE_OUT_OF_LIMIT);
            }
            if (errCode ==  WxErrCode.MEDIA_COUNT_OUT_OF_LIMIT) {
                throw new BusinessException(ErrorCode.MEDIA_COUNT_OUT_OF_LIMIT);
            }
            logger.error("[微信] 上传永久图片素材失败: result:{} url:{}", returnJson, imgUrl);
            throw new BusinessException(ErrorCode.OPERATION_FAILED, "上传图片失败，请重试");
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("mediaId", returnJson.getString("media_id"));
        map.put("url", returnJson.getString("url"));
        return map;
    }

    /**
     * 上传图文中的图片到微信服务器
     */
    public static String uploadImgForPost(String accessToken, String imgUrl) throws DownloadFileFailedException {

        // 获取内部地址
        Map<String ,String> address = UrlUtil.getPicIntranetAddress(imgUrl);
        String result = HttpClientUtil.postFile(WxApiConfig.getAddPostImageUrl(accessToken), address.get("url"), address.get("host"));

        JSONObject returnJson = new JSONObject(result);
        int errCode = returnJson.optInt("errcode");
        if (errCode != 0) {
            if (errCode == WxErrCode.MEDIA_SIZE_OUT_OF_LIMIT) {
                throw new BusinessException(ErrorCode.MEDIA_SIZE_OUT_OF_LIMIT);
            }
            logger.error("[Weixin:uploadImgForPost] 上传图文中图片失败: result:{} imgUrl:{}", returnJson, imgUrl);
            throw new BusinessException(ErrorCode.OPERATION_FAILED, "上传内容中图片失败，请重试");
        }
        return returnJson.getString("url");

    }

    /**
     * 上传多图文到微信
     * @param posts 多图文对象
     * @return 图文mediaId
     */
    public static String uploadPosts(String accessToken, List<PostPO> posts) {

        // 组装articles
        JSONArray jsonArray = new JSONArray();
        for (PostPO postPO : posts) {
            JSONObject postJson = new JSONObject();
            postJson.put("title", postPO.getTitle());
            postJson.put("thumb_media_id", postPO.getThumbMediaId());
            postJson.put("author", postPO.getAuthor());
            postJson.put("digest", postPO.getDigest());
            postJson.put("show_cover_pic", postPO.getShowCoverPic());
            postJson.put("content", postPO.getContent());
            postJson.put("content_source_url", postPO.getContentSourceUrl());

            jsonArray.put(postJson);
        }

        // 上传
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("articles", jsonArray);

        String jsonStr;
        try {
            jsonStr = JsonUtil.bean2String(jsonObject.toMap());
        } catch (JsonConvertException e) {
            logger.error("[WeiXin:uploadPosts] bean to string failed, JsonObject: {}", jsonObject, e);
            throw new BusinessException(ErrorCode.OPERATION_FAILED, "上传图文到微信失败，请稍后再试");
        }
        String result = HttpClientUtil.postJson(WxApiConfig.getCreatePostsUrl(accessToken), jsonStr);
        if (result == null) {
            logger.error("[WeiXin:uploadPosts] return is null");
            throw new BusinessException(ErrorCode.OPERATION_FAILED, "上传图文到微信失败，请稍后再试");
        }

        JSONObject returnJson = new JSONObject(result);

        int errCode = returnJson.optInt("errcode");
        if (errCode != 0) {
            // 封面图id不存在
            if (errCode == 40007) {
                throw new BusinessException(ErrorCode.THUMB_MEDIA_ID_NOT_EXIST_ERROR);
            }
            // 未知错误
            logger.error("[WeiXin:uploadPosts] upload posts failed, result:{} data:{}", returnJson, jsonObject);
            throw new BusinessException(ErrorCode.OPERATION_FAILED, "上传图文到微信失败，请稍后再试");
        }
        return returnJson.getString("media_id");
    }

    /**
     * 更新微信图文的某一篇
     */
    public static void updatePost(String accessToken, String mediaId, PostPO postPO) throws WxMediaIdNotExistException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("media_id", mediaId);
        jsonObject.put("index", postPO.getIndex());

        // 组装articles
        JSONObject postJson = new JSONObject();
        postJson.put("title", postPO.getTitle());
        postJson.put("thumb_media_id", postPO.getThumbMediaId());
        postJson.put("author", postPO.getAuthor());
        postJson.put("digest", postPO.getDigest());
        postJson.put("show_cover_pic", postPO.getShowCoverPic());
        postJson.put("content", postPO.getContent());
        postJson.put("content_source_url", postPO.getContentSourceUrl());

        jsonObject.put("articles", postJson);

        String jsonStr;
        try {
            jsonStr = JsonUtil.bean2String(jsonObject.toMap());
        } catch (JsonConvertException e) {
            logger.error("[WeiXin:updatePost] bean to string failed, JsonObject: {}", jsonObject, e);
            throw new BusinessException(ErrorCode.OPERATION_FAILED, "修改微信图文失败，请稍后再试");
        }
        String result = HttpClientUtil.postJson(WxApiConfig.getUpdatePostUrl(accessToken), jsonStr);
        if (result == null) {
            logger.error("[WeiXin:updatePost] http return is null");
            throw new BusinessException(ErrorCode.OPERATION_FAILED, "修改微信图文失败，请稍后再试");
        }
        JSONObject returnJson = new JSONObject(result);

        int errCode = returnJson.optInt("errcode");
        if (errCode != 0) {
            if (errCode == 40007) {
                throw new WxMediaIdNotExistException();
            }
            // 多图文数目不一致，修改了不存在的index
            if (errCode == 40114) {
                throw new BusinessException(ErrorCode.DIFFERENT_POSTS_NUM_ERROR);
            }
            logger.error("[WeiXin:updatePost] update post failed, result:{} data:{}", returnJson, jsonObject);
            throw new BusinessException(ErrorCode.OPERATION_FAILED, "修改微信图文失败，请稍后再试");
        }

    }

    /**
     * 根据偏移获取微信图文列表
     */
    public static WxPostListDTO getWxPostList(String accessToken, int offset, int count) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "news");
        jsonObject.put("offset", offset);
        jsonObject.put("count", count);
        String result = HttpClientUtil.postJson(WxApiConfig.getBatchMaterialUrl(accessToken), jsonObject.toString());
        if (result == null) {
            throw new WxPostListGetException("[WeiXin:getWxPostList] failed, result is null");
        }

        JSONObject returnJson = new JSONObject(result);
        if (returnJson.has("errcode")) {
            String msg = "[WeiXin:getWxPostList] unknown errorCode, param: " + jsonObject + " result: " +returnJson;
            throw new WxPostListGetException(msg);
        }
        try {
            return JsonUtil.string2Bean(result, WxPostListDTO.class);
        } catch (JsonConvertException e) {
            String msg = "[WeiXin:getWxPostList] serializing failed , param: " + jsonObject + " result: " +returnJson;
            throw new WxPostListGetException(msg, e);
        }
    }

    /**
     * 根据mediaId获取微信图文
     */
    public static List<WxPostDTO> getWxPost(String mediaId, String accessToken) {
        List<WxPostDTO> wxPostDTOS = new ArrayList<>();

        JSONObject params = new JSONObject();
        params.put("media_id", mediaId);
        String result = HttpClientUtil.postJson(WxApiConfig.getMaterialUrl(accessToken), params.toString());
        JSONObject returnJson = new JSONObject(result);

        if (returnJson.optInt("errcode") == 40007) {
            throw new BusinessException(ErrorCode.WX_POST_DELETED_ERROR);
        }

        JSONArray postArray = returnJson.getJSONArray("news_item");
        for (int i = 0; i < postArray.length(); i++) {
            JSONObject jsonObject = postArray.getJSONObject(i);
            WxPostDTO wxPostDTO;
            try {
                wxPostDTO = JsonUtil.string2Bean(jsonObject.toString(), WxPostDTO.class);
            } catch (JsonConvertException e) {
                throw new RuntimeException("序列化wxPostDTO失败", e);
            }
            wxPostDTOS.add(wxPostDTO);
        }
        return wxPostDTOS;
    }
}
