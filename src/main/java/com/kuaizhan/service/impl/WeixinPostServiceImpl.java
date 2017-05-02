package com.kuaizhan.service.impl;


import com.kuaizhan.config.WxApiConfig;
import com.kuaizhan.dao.redis.RedisImageDao;
import com.kuaizhan.exception.business.*;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.WxPostListDTO;
import com.kuaizhan.pojo.DTO.WxPostDTO;
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
    public void deletePost(String mediaId, String accessToken) throws MaterialDeleteException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("media_id", mediaId);
        String result = HttpClientUtil.postJson(WxApiConfig.deleteMaterialUrl(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);
        // 40007错误码是已经被删除，不报错
        int errCode = returnJson.optInt("errcode");

        if (errCode == 48005 ) {
            throw new MaterialDeleteException("自定义菜单或自动回复中包含该图文，无法删除。");
        }
        if (errCode != 0 && returnJson.optInt("errcode") != 40007) {
            logger.error("[微信] 删除多图文失败: result: " + returnJson);
            throw new MaterialDeleteException();
        }
    }

    @Override
    public HashMap<String, String> uploadImage(String accessToken, String imgUrl) throws AddMaterialException, DownloadFileFailedException {
        // 处理没有http头的问题

        imgUrl = UrlUtil.fixQuote(imgUrl);
        imgUrl = UrlUtil.fixProtocol(imgUrl);

        // 获取内部地址
        Map<String ,String> address = UrlUtil.getPicIntranetAddress(imgUrl);
        logger.info("[微信] 上传图片素材, address: " + address);
        String result = HttpClientUtil.postFile(WxApiConfig.addMaterialUrl(accessToken, "image"), address.get("url"), address.get("host"));

        JSONObject returnJson = new JSONObject(result);
        if (returnJson.has("errcode")) {
            logger.error("[微信] 上传永久图片素材失败: result: " + returnJson + " url:" + imgUrl);
            throw new AddMaterialException();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("mediaId", returnJson.getString("media_id"));
        map.put("url", returnJson.getString("url"));
        return map;
    }

    @Override
    public String uploadImgForPost(String accessToken, String imgUrl) throws AddMaterialException, RedisException, DownloadFileFailedException {
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
            throw new AddMaterialException();
        }
        wxUrl = returnJson.getString("url");

        // 缓存到redis
        redisImageDao.setImageUrl(imgUrl, wxUrl);
        return wxUrl;
    }

    @Override
    public String uploadPosts(String accessToken, List<PostDO> posts) throws UploadPostsException, ThumbMediaIdNotExistException {

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
        String result = HttpClientUtil.postJson(WxApiConfig.getCreatePostsUrl(accessToken), jsonObject.toString());
        if (result == null) {
            throw new RuntimeException("[微信] 上传图文返回体为空");
        }
        JSONObject returnJson = new JSONObject(result);
        logger.info("[微信] 上传多图文结束, param: " + jsonObject + ", result: "  + returnJson);

        int errCode = returnJson.optInt("errcode");
        if (errCode != 0) {
            if (errCode == 40007) {
                throw new ThumbMediaIdNotExistException();
            }
            // 未知错误
            logger.error("[微信] 上传多图文失败, result:"+ returnJson + " data:" + jsonObject);
            throw new UploadPostsException();
        }
        return returnJson.getString("media_id");
    }

    @Override
    public void updatePost(String accessToken, String mediaId, PostDO postDO) throws UploadPostsException, MediaIdNotExistException, WxPostLessThenPost {
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

        String result = HttpClientUtil.postJson(WxApiConfig.getUpdatePostUrl(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);

        int errCode = returnJson.optInt("errcode");
        if (errCode != 0) {
            if (errCode == 40007) {
                throw new MediaIdNotExistException();
            }
            if (errCode == 40114) {
                throw new WxPostLessThenPost();
            }
            logger.error("[微信] 修改多图文失败, result:"+ returnJson + " data:" + jsonObject);
            throw new UploadPostsException();
        }

    }

    @Override
    public WxPostListDTO getWxPostList(String accessToken, int offset, int count) throws MaterialGetException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "news");
        jsonObject.put("offset", offset);
        jsonObject.put("count", count);
        String result = HttpClientUtil.postJson(WxApiConfig.getBatchMaterialUrl(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);
        if (returnJson.has("errcode")) {
            logger.error("[微信获取素材错误], param: " + jsonObject + " result: " +returnJson);
            throw new MaterialGetException();
        }
        try {
            WxPostListDTO wxPostListDTO = JsonUtil.<WxPostListDTO>string2Bean(result, WxPostListDTO.class);
            return wxPostListDTO;
        } catch (IOException e) {
            // FIXME: 这么搞，有效错误信息都丢失啦
            throw new MaterialGetException();
        }
    }

    @Override
    public List<WxPostListDTO> listAllPosts(String accessToken) throws MaterialGetException {
        List<WxPostListDTO> wxPostListDTOList = new LinkedList<>();
        int MAX_MATERIAL_COUNT_EACH_FETCH = 20;
        WxPostListDTO wxPostListDTO = getWxPostList(accessToken, 0, MAX_MATERIAL_COUNT_EACH_FETCH);
        wxPostListDTOList.add(wxPostListDTO);
        int index = wxPostListDTO.getItemCount(), totalCount = wxPostListDTO.getTotalCount();
        while (index < totalCount) {
            try {
                wxPostListDTO = getWxPostList(accessToken, index, MAX_MATERIAL_COUNT_EACH_FETCH);
                wxPostListDTOList.add(wxPostListDTO);
            } catch (MaterialGetException e) {
                LogUtil.logMsg(e);
            } finally {
                index += MAX_MATERIAL_COUNT_EACH_FETCH;
            }
        }
        return wxPostListDTOList;
    }

    @Override
    public List<WxPostDTO> getWxPost(String mediaId, String accessToken) throws WxPostDeletedException {
        List<WxPostDTO> wxPostDTOS = new ArrayList<>();

        JSONObject params = new JSONObject();
        params.put("media_id", mediaId);
        String result = HttpClientUtil.postJson(WxApiConfig.getMaterialUrl(accessToken), params.toString());
        JSONObject returnJson = new JSONObject(result);

        if (returnJson.optInt("errcode") == 40007) {
            throw new WxPostDeletedException();
        }

        JSONArray postArray = returnJson.getJSONArray("news_item");
        for (int i = 0; i < postArray.length(); i++) {
            JSONObject jsonObject = postArray.getJSONObject(i);
            WxPostDTO wxPostDTO;
            try {
                wxPostDTO = JsonUtil.string2Bean(jsonObject.toString(), WxPostDTO.class);
            } catch (IOException e) {
                throw new RuntimeException("序列化wxPostDTO失败", e);
            }
            wxPostDTOS.add(wxPostDTO);
        }
        return wxPostDTOS;
    }
}
