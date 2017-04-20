package com.kuaizhan.service.impl;


import com.kuaizhan.config.ApiConfig;
import com.kuaizhan.dao.redis.RedisImageDao;
import com.kuaizhan.exception.business.*;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.PostDTO;
import com.kuaizhan.service.WeixinPostService;
import com.kuaizhan.utils.HttpClientUtil;
import com.kuaizhan.utils.JsonUtil;
import com.kuaizhan.utils.LogUtil;
import com.kuaizhan.utils.UrlUtil;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Created by liangjiateng on 2017/3/28.
 */
@Service("weixinPostService")
public class WeixinPostServiceImpl implements WeixinPostService {

    @Resource
    RedisImageDao redisImageDao;

    private static Logger logger = Logger.getLogger(WeixinPostServiceImpl.class);

    @Override
    public void deletePost(String mediaId, String accessToken) throws MaterialDeleteException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("media_id", mediaId);
        String result = HttpClientUtil.postJson(ApiConfig.deleteMaterialUrl(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);
        // 40007错误码是已经被删除，不报错
        if (returnJson.optInt("errcode") != 0 && returnJson.optInt("errcode") != 40007) {
            logger.error("[微信] 删除多图文失败: result: " + returnJson);
            throw new MaterialDeleteException();
        }
    }

    @Override
    public HashMap<String, String> uploadImage(String accessToken, String imgUrl) throws AddMaterialException, DownloadFileFailedException {
        // 处理没有http头的问题

        imgUrl = UrlUtil.fixProtocol(imgUrl);
        imgUrl = UrlUtil.fixQuote(imgUrl);

        // 获取内部地址
        Map<String ,String> address = UrlUtil.getPicIntranetAddress(imgUrl);
        logger.info("[微信] 上传图片素材, address: " + address);
        String result = HttpClientUtil.postMedia(ApiConfig.addMaterialUrl(accessToken, "image"), address.get("url"), address.get("host"));

        JSONObject returnJson = new JSONObject(result);
        if (returnJson.has("errcode")) {
            logger.error("[微信] 上传永久图片素材失败: result: " + returnJson);
            throw new AddMaterialException();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("mediaId", returnJson.getString("media_id"));
        map.put("url", returnJson.getString("url"));
        return map;
    }

    @Override
    public String uploadImgForPost(String accessToken, String imgUrl) throws AddMaterialException, RedisException, DownloadFileFailedException {
        imgUrl = UrlUtil.fixProtocol(imgUrl);
        imgUrl = UrlUtil.fixQuote(imgUrl);

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
        System.out.println("---->" + address);
        String result = HttpClientUtil.postMedia(ApiConfig.getAddPostImageUrl(accessToken), address.get("url"), address.get("host"));

        JSONObject returnJson = new JSONObject(result);
        if (returnJson.has("errcode")) {
            logger.error("[微信] 上传图文中图片失败: result: " + returnJson);
            throw new AddMaterialException();
        }
        wxUrl = returnJson.getString("url");

        // 缓存到redis
        redisImageDao.setImageUrl(imgUrl, wxUrl);
        return wxUrl;
    }

    @Override
    public String uploadPosts(String accessToken, List<PostDO> posts) throws UploadPostsException{

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
        String result = HttpClientUtil.postJson(ApiConfig.getCreatePostsUrl(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);
        logger.info("[微信] 上传多图文结束, param: " + jsonObject + ", result: "  + returnJson);

        if (returnJson.optInt("errcode") != 0) {
            logger.error("[微信] 上传多图文失败, data:"+ jsonObject + " result:" + returnJson);
            throw new UploadPostsException();
        }
        return returnJson.getString("media_id");
    }

    @Override
    public void updatePost(String accessToken, String mediaId, PostDO postDO) throws UploadPostsException, MediaIdNotExistException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("media_id", mediaId);
        jsonObject.put("index", 0); // 只更新单图文, 都是0

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

        String result = HttpClientUtil.postJson(ApiConfig.getUpdatePostUrl(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);

        if (returnJson.optInt("errcode") != 0) {
            if (returnJson.optInt("errcode") == 40007) {
                throw new MediaIdNotExistException();
            }
            // TODO: 考虑media、thumbMediaId已经被删除的异常情况, 40007
            logger.error("[微信] 修改图文失败, data:"+ jsonObject + " result:" + returnJson);
            throw new UploadPostsException();
        }

    }

    @Override
    public PostDTO getPostDTOByOffset(String accessToken, int offset, int count) throws MaterialGetException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "news");
        jsonObject.put("offset", offset);
        jsonObject.put("count", count);
        String result = HttpClientUtil.postJson(ApiConfig.getMaterial(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);
        if (returnJson.has("errcode")) {
            logger.error("[微信获取素材错误], param: " + jsonObject + " result: " +returnJson);
            throw new MaterialGetException();
        }
        try {
            PostDTO postDTO = JsonUtil.<PostDTO>string2Bean(result, PostDTO.class);
            return postDTO;
        } catch (IOException e) {
            throw new MaterialGetException();
        }
    }

    @Override
    public List<PostDTO> listAllPosts(String accessToken) throws MaterialGetException {
        List<PostDTO> postDTOList = new LinkedList<>();
        int MAX_MATERIAL_COUNT_EACH_FETCH = 20;
        PostDTO postDTO = getPostDTOByOffset(accessToken, 0, MAX_MATERIAL_COUNT_EACH_FETCH);
        postDTOList.add(postDTO);
        int index = postDTO.getItemCount(), totalCount = postDTO.getTotalCount();
        while (index < totalCount) {
            try {
                postDTO = getPostDTOByOffset(accessToken, index, MAX_MATERIAL_COUNT_EACH_FETCH);
                postDTOList.add(postDTO);
            } catch (MaterialGetException e) {
                LogUtil.logMsg(e);
            } finally {
                index += MAX_MATERIAL_COUNT_EACH_FETCH;
            }
        }
        return postDTOList;
    }
}
