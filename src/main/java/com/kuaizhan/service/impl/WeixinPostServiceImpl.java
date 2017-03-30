package com.kuaizhan.service.impl;


import com.kuaizhan.config.ApiConfig;
import com.kuaizhan.exception.business.AddMaterialException;
import com.kuaizhan.exception.business.MaterialDeleteException;
import com.kuaizhan.exception.business.UploadPostsException;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.service.WeixinPostService;
import com.kuaizhan.utils.HttpClientUtil;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


/**
 * Created by liangjiateng on 2017/3/28.
 */
@Service("weixinPostService")
public class WeixinPostServiceImpl implements WeixinPostService {

    private static Logger logger = Logger.getLogger(WeixinPostServiceImpl.class);

    @Override
    public void deletePost(String mediaId, String accessToken) throws MaterialDeleteException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("media_id", mediaId);
        String result = HttpClientUtil.postJson(ApiConfig.deleteMaterialUrl(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);
        if (returnJson.getInt("errcode") != 0) {
            throw new MaterialDeleteException();
        }
    }

    @Override
    public HashMap<String, String> uploadImage(String accessToken, String imgUrl) throws AddMaterialException {
        String result = HttpClientUtil.postMedia(ApiConfig.addMaterialUrl(accessToken, "image"), imgUrl);
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
    public String uploadImgForPost(String accessToken, String imgUrl) throws AddMaterialException {
        String result = HttpClientUtil.postMedia(ApiConfig.getAddPostImageUrl(accessToken), imgUrl);
        JSONObject returnJson = new JSONObject(result);
        if (returnJson.has("errcode")) {
            logger.error("[微信] 上传图文中图片失败: result: " + returnJson);
            throw new AddMaterialException();
        }
        return returnJson.getString("url");
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

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("articles", jsonArray);

        // 上传
        logger.info("[微信] 上传多图文开始, data: " + jsonObject);
        String result = HttpClientUtil.postJson(ApiConfig.getCreatePostsUrl(accessToken), jsonObject.toString());

        JSONObject returnJson = new JSONObject(result);
        logger.info("[微信] 上传多图文结束, result:" + returnJson);

        if (returnJson.optInt("errcode") != 0) {
            logger.error("[微信] 上传多图文失败, result:" + returnJson);
            throw new UploadPostsException();
        }
        return returnJson.getString("media_id");
    }


}
