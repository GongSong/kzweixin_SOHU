package com.kuaizhan.manager;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.config.KzApiConfig;
import com.kuaizhan.exception.common.GetKzArticleException;
import com.kuaizhan.exception.common.KZPicUploadException;
import com.kuaizhan.pojo.dto.ArticleDTO;
import com.kuaizhan.utils.HttpClientUtil;
import com.kuaizhan.utils.JsonUtil;
import com.kuaizhan.utils.UrlUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 对主站Api的封装和异常转换
 * Created by zixiong on 2017/5/10.
 */
public class KzManager {

    /**
     * 获取根据pageId获取快文
     */
    public static ArticleDTO getKzArticle(long pageId) throws GetKzArticleException{
        String result = HttpClientUtil.get(KzApiConfig.getKzArticleUrl(pageId));

        if (result == null) {
            throw new GetKzArticleException("[Kz:getKzArticle] result is null, pageId:" + pageId);
        }
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            throw new GetKzArticleException("[Kz:getKzArticle] json parse failed, pageId:" + pageId, e);
        }
        ArticleDTO articleDTO = null;
        if (jsonObject.getInt("ret") == 0) {
            articleDTO = JsonUtil.string2Bean(jsonObject.get("data").toString(), ArticleDTO.class);
        }
        return articleDTO;
    }

    /**
     * 把外部图片上传到主站，转换为快站链接
     */
    public static String uploadPicToKz(String url, long userId) throws KZPicUploadException {
        Map<String, Object> params = new HashMap<>();
        params.put("img_url", url);
        params.put("uid", userId);
        // 指定host
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", ApplicationConfig.KZ_SERVICE_HOST);

        String result = HttpClientUtil.post(KzApiConfig.KZ_UPLOAD_PIC_URL, params, headers);
        if (result == null) {
            String msg = "[上传图片到快站] 上传失败，url: " +  KzApiConfig.KZ_UPLOAD_PIC_URL + " param: " + params + "headers: " + headers;
            throw new KZPicUploadException(msg);
        }

        JSONObject returnJson;
        try {
            returnJson = new JSONObject(result);
        }catch (JSONException e){
            String msg = "[上传图片到快站] 上传失败，url: " +  KzApiConfig.KZ_UPLOAD_PIC_URL + " param: " + params + "headers: " + headers + " result: " + result;
            throw new KZPicUploadException(msg, e);
        }

        if (returnJson.getInt("ret") == 0) {
            JSONObject data = returnJson.getJSONObject("data");
            return UrlUtil.fixProtocol(data.getString("url"));
        } else {
            String msg = "[上传图片到快站] 上传失败，url: " +  KzApiConfig.KZ_UPLOAD_PIC_URL + " param: " + params + "headers: " + headers + " result: " + result;
            throw new KZPicUploadException(msg);
        }
    }
}
