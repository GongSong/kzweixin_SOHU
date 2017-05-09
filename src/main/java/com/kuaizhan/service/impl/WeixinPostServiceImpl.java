package com.kuaizhan.service.impl;


import com.kuaizhan.config.WxApiConfig;
import com.kuaizhan.constant.ErrorCodes;
import com.kuaizhan.dao.redis.RedisImageDao;
import com.kuaizhan.exception.BusinessException;
import com.kuaizhan.exception.common.*;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.WxPostListDTO;
import com.kuaizhan.pojo.DTO.WxPostDTO;
import com.kuaizhan.service.WeixinPostService;
import com.kuaizhan.utils.HttpClientUtil;
import com.kuaizhan.utils.JsonUtil;
import com.kuaizhan.utils.UrlUtil;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


/**
 * Created by liangjiateng on 2017/3/28.
 */
@Service("weixinPostService")
public class WeixinPostServiceImpl implements WeixinPostService {

    @Resource
    RedisImageDao redisImageDao;

    private static Logger logger = Logger.getLogger(WeixinPostServiceImpl.class);

    @Override
    public void deletePost(String mediaId, String accessToken) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("media_id", mediaId);

        String result = HttpClientUtil.postJson(WxApiConfig.deleteMaterialUrl(accessToken), jsonObject.toString());
        if (result == null) {
            logger.error("[WeixinPostServiceImpl.deletePost] 删除图文返回为null, mediaId: " + mediaId);
            throw new BusinessException(ErrorCodes.OPERATION_FAILED, "删除图文失败, 请稍后重试");
        }

        JSONObject returnJson = new JSONObject(result);
        int errCode = returnJson.optInt("errcode");
        if (errCode != 0) {
            // post被使用而不能被删除
            if (errCode == 48005) {
                throw new BusinessException(ErrorCodes.POST_USED_BY_OTHER_ERROR);
            }
            // 40007错误码是已经被删除，不报错
            else if (errCode == 40007) return;
            else {
                // 未知错误
                logger.error("[WeixinPostServiceImpl.deletePost] 删除多图文失败, mediaId:" + mediaId + " result: " + returnJson);
                throw new BusinessException(ErrorCodes.OPERATION_FAILED, "删除图文失败, 请稍后重试");
            }
        }
    }

    @Override
    public HashMap<String, String> uploadImage(String accessToken, String imgUrl) throws DownloadFileFailedException {
        // 处理没有http头的问题

        imgUrl = UrlUtil.fixQuote(imgUrl);
        imgUrl = UrlUtil.fixProtocol(imgUrl);

        // 获取内部地址
        Map<String ,String> address = UrlUtil.getPicIntranetAddress(imgUrl);
        logger.info("[微信] 上传图片素材, address: " + address);
        String result = HttpClientUtil.postFile(WxApiConfig.addMaterialUrl(accessToken, "image"), address.get("url"), address.get("host"));

        JSONObject returnJson = new JSONObject(result);
        int errCode = returnJson.optInt("errcode");
        if (errCode != 0) {
            if (errCode == 45001) {
                // TODO: 被controller直接调用时合理，被其他service调用时不合理
                throw new BusinessException(ErrorCodes.MEDIA_SIZE_OUT_OF_LIMIT);
            }
            logger.error("[微信] 上传永久图片素材失败: result: " + returnJson + " url:" + imgUrl);
            throw new BusinessException(ErrorCodes.OPERATION_FAILED, "上传图片失败，请重试");
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("mediaId", returnJson.getString("media_id"));
        map.put("url", returnJson.getString("url"));
        return map;
    }

    @Override
    public String uploadImgForPost(String accessToken, String imgUrl) throws DownloadFileFailedException {
        imgUrl = UrlUtil.fixQuote(imgUrl);
        imgUrl = UrlUtil.fixProtocol(imgUrl);

        // 本来就是微信url的不再上传
        if (imgUrl.indexOf("https://mmbiz") == 0 || imgUrl.indexOf("http://mmbiz") == 0) {
            return imgUrl;
        }

        // 先从redis中取
        String wxUrl = redisImageDao.getImageUrl(imgUrl);
        if (wxUrl != null) {
            return wxUrl;
        }

        // 获取内部地址
        Map<String ,String> address = UrlUtil.getPicIntranetAddress(imgUrl);
        String result = HttpClientUtil.postFile(WxApiConfig.getAddPostImageUrl(accessToken), address.get("url"), address.get("host"));

        JSONObject returnJson = new JSONObject(result);
        if (returnJson.has("errcode")) {
            logger.error("[微信] 上传图文中图片失败: result: " + returnJson + " imgUrl: " + imgUrl);
            throw new BusinessException(ErrorCodes.OPERATION_FAILED, "上传内容中图片失败，请重试");
        }
        wxUrl = returnJson.getString("url");

        // 缓存到redis
        redisImageDao.setImageUrl(imgUrl, wxUrl);
        return wxUrl;
    }

    @Override
    public String uploadPosts(String accessToken, List<PostDO> posts) {

        // 组装articles
        JSONArray jsonArray = new JSONArray();
        for (PostDO postDO : posts) {
            JSONObject postJson = new JSONObject();
            postJson.put("title", postDO.getTitle());
            postJson.put("thumb_media_id", postDO.getThumbMediaId());
            postJson.put("author", postDO.getAuthor());
            postJson.put("digest", postDO.getDigest());
            postJson.put("show_cover_pic", postDO.getShowCoverPic());
            postJson.put("content", postDO.getContent());
            postJson.put("content_source_url", postDO.getContentSourceUrl());

            jsonArray.put(postJson);
        }

        // 上传
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("articles", jsonArray);

        String jsonStr;
        try {
            jsonStr = JsonUtil.bean2String(jsonObject.toMap());
        } catch (Bean2StringFailedException e) {
            logger.error("[WeiXin:uploadPosts] bean to string failed" + jsonObject, e);
            throw new BusinessException(ErrorCodes.OPERATION_FAILED, "上传图文到微信失败，请稍后再试");
        }
        String result = HttpClientUtil.postJson(WxApiConfig.getCreatePostsUrl(accessToken), jsonStr);
        if (result == null) {
            logger.error("[WeiXin:uploadPosts] return is null");
            throw new BusinessException(ErrorCodes.OPERATION_FAILED, "上传图文到微信失败，请稍后再试");
        }

        JSONObject returnJson = new JSONObject(result);

        int errCode = returnJson.optInt("errcode");
        if (errCode != 0) {
            // 封面图id不存在
            if (errCode == 40007) {
                throw new BusinessException(ErrorCodes.THUMB_MEDIA_ID_NOT_EXIST_ERROR);
            }
            // 未知错误
            logger.error("[WeiXin:uploadPosts] upload posts failed, result:"+ returnJson + " data:" + jsonObject);
            throw new BusinessException(ErrorCodes.OPERATION_FAILED, "上传图文到微信失败，请稍后再试");
        }
        return returnJson.getString("media_id");
    }

    @Override
    public void updatePost(String accessToken, String mediaId, PostDO postDO) throws MediaIdNotExistException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("media_id", mediaId);
        jsonObject.put("index", postDO.getIndex());

        // 组装articles
        JSONObject postJson = new JSONObject();
        postJson.put("title", postDO.getTitle());
        postJson.put("thumb_media_id", postDO.getThumbMediaId());
        postJson.put("author", postDO.getAuthor());
        postJson.put("digest", postDO.getDigest());
        postJson.put("show_cover_pic", postDO.getShowCoverPic());
        postJson.put("content", postDO.getContent());
        postJson.put("content_source_url", postDO.getContentSourceUrl());

        jsonObject.put("articles", postJson);

        String jsonStr;
        try {
            jsonStr = JsonUtil.bean2String(jsonObject.toMap());
        } catch (Bean2StringFailedException e) {
            logger.error("[WeiXin:updatePost] bean to string failed" + jsonObject, e);
            throw new BusinessException(ErrorCodes.OPERATION_FAILED, "修改微信图文失败，请稍后再试");
        }
        String result = HttpClientUtil.postJson(WxApiConfig.getUpdatePostUrl(accessToken), jsonStr);
        if (result == null) {
            logger.error("[WeiXin:updatePost] http return is null");
            throw new BusinessException(ErrorCodes.OPERATION_FAILED, "修改微信图文失败，请稍后再试");
        }
        JSONObject returnJson = new JSONObject(result);

        int errCode = returnJson.optInt("errcode");
        if (errCode != 0) {
            if (errCode == 40007) {
                throw new MediaIdNotExistException();
            }
            // 多图文数目不一致，修改了不存在的index
            if (errCode == 40114) {
                throw new BusinessException(ErrorCodes.DIFFERENT_POSTS_NUM_ERROR);
            }
            logger.error("[WeiXin:updatePost] update post failed, result:"+ returnJson + " data:" + jsonObject);
            throw new BusinessException(ErrorCodes.OPERATION_FAILED, "修改微信图文失败，请稍后再试");
        }

    }

    @Override
    public WxPostListDTO getWxPostList(String accessToken, int offset, int count) {
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
        } catch (String2BeanFailedException e) {
            String msg = "[WeiXin:getWxPostList] serializing failed , param: " + jsonObject + " result: " +returnJson;
            throw new WxPostListGetException(msg, e);
        }
    }

    @Override
    public List<WxPostDTO> getWxPost(String mediaId, String accessToken) {
        List<WxPostDTO> wxPostDTOS = new ArrayList<>();

        JSONObject params = new JSONObject();
        params.put("media_id", mediaId);
        String result = HttpClientUtil.postJson(WxApiConfig.getMaterialUrl(accessToken), params.toString());
        JSONObject returnJson = new JSONObject(result);

        if (returnJson.optInt("errcode") == 40007) {
            throw new BusinessException(ErrorCodes.WX_POST_DELETED_ERROR);
        }

        JSONArray postArray = returnJson.getJSONArray("news_item");
        for (int i = 0; i < postArray.length(); i++) {
            JSONObject jsonObject = postArray.getJSONObject(i);
            WxPostDTO wxPostDTO;
            try {
                wxPostDTO = JsonUtil.string2Bean(jsonObject.toString(), WxPostDTO.class);
            } catch (String2BeanFailedException e) {
                throw new RuntimeException("序列化wxPostDTO失败", e);
            }
            wxPostDTOS.add(wxPostDTO);
        }
        return wxPostDTOS;
    }
}
