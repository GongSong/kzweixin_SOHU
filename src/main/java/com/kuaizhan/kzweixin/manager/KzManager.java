package com.kuaizhan.kzweixin.manager;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.config.ApplicationConfig;
import com.kuaizhan.kzweixin.config.KzApiConfig;
import com.kuaizhan.kzweixin.exception.kuaizhan.Export2KzException;
import com.kuaizhan.kzweixin.exception.kuaizhan.GetKzArticleException;
import com.kuaizhan.kzweixin.exception.kuaizhan.KZPicUploadException;
import com.kuaizhan.kzweixin.exception.kuaizhan.KzApiException;
import com.kuaizhan.kzweixin.entity.post.ArticleDTO;
import com.kuaizhan.kzweixin.dao.po.PostPO;
import com.kuaizhan.kzweixin.utils.*;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.File;
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

        Map<String, String> headers = new HashMap<>();
        headers.put("Host", ApplicationConfig.KZ_SERVICE_HOST);
        String result = HttpClientUtil.get(KzApiConfig.getKzArticleUrl(pageId), headers);

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
     * @throws KZPicUploadException 图片格式不对，以及各种未知原因导致的图片上传失败
     */
    public static String uploadPicToKz(String url) throws KZPicUploadException {

        String fileName = HttpClientUtil.downloadFile(url);
        File file = new File(fileName);

        HttpResponse<JsonNode> jsonResponse;
        try {
             jsonResponse = Unirest.post(KzApiConfig.KZ_UPLOAD_PIC_V2)
                    .field("file", file)
                    .asJson();
        } catch (UnirestException e) {
            throw new KZPicUploadException("[uploadPicToKz] upload failed");
        } finally {
            file.delete();
        }

        if (jsonResponse.getStatus() == 200) {
            JSONObject jsonResult = jsonResponse.getBody().getObject();
            return KzApiConfig.KZ_PIC_DOMAIN + jsonResult.getJSONObject("data").getString("url");
        } else {
            throw new KZPicUploadException("[uploadPicToKz] status code not 200, jsonResponse:" + jsonResponse);
        }
    }

    /**
     * 导出图文到快站文章
     * @throws Export2KzException 快站返回的不是预期结果
     */
    public static void export2KzArticle(long siteId, long categoryId, PostPO postPO, String content) throws Export2KzException {
        Map<String, Object> param = new HashMap<>();
        param.put("site_id", siteId);
        param.put("post_category_id", categoryId);
        param.put("post_title", postPO.getTitle());
        param.put("post_desc", postPO.getDigest());
        param.put("pic_url", UrlUtil.fixProtocol(postPO.getThumbUrl()));
        param.put("post_content", content);

        String result = HttpClientUtil.post(KzApiConfig.KZ_POST_ARTICLE_URL, param);
        if (result == null) {
            throw new Export2KzException("[Kz:export2KzArticle] result is null, pageId:" + postPO.getPageId());
        }
        JSONObject returnJson;
        try {
            returnJson = new JSONObject(result);
        } catch (JSONException e) {
            throw new Export2KzException("[Kz:export2KzArticle] JsonParse error, " +
                    " pageId:" + postPO.getPageId() +
                    " result:" + result +
                    " siteId:" + siteId +
                    " categoryId:" + categoryId +
                    " title:" + postPO.getTitle(), e);
        }
        if (returnJson.getInt("ret") != 0) {
            throw new Export2KzException("[Kz:export2KzArticle] return code error, pageId:" + postPO.getPageId() + " result:" + result);
        }
    }

    /**
     * 申请快站推送token
     * @return clientId和token组成的map
     */
    public static Map<String, String> applyPushToken() {
        int timestamp = DateUtil.curSeconds();
        String sign = EncryptUtil.md5(ApplicationConfig.KZ_PUSH_ACCESS_ID + ApplicationConfig.KZ_PUSH_ACCESS_KEY + timestamp);

        Map<String, Object> param = new HashMap<>();
        param.put("accessId", ApplicationConfig.KZ_PUSH_ACCESS_ID);
        param.put("timestamp", timestamp);
        param.put("sign", sign);
        param.put("ttl", 30 * 60);

        String result = HttpClientUtil.postJson(KzApiConfig.KZ_APPLY_PUSH_TOKEN, JsonUtil.bean2String(param));
        if (result == null) {
            throw new KzApiException("[Kz:applyPushToken] result is null");
        }
        JSONObject returnJson;
        try {
            returnJson = new JSONObject(result);
        } catch (JSONException e) {
            throw new KzApiException("[Kz:applyPushToken] Json Parse Error, result:" + result);
        }
        int code = returnJson.optInt("code");
        if (code != 0) {
            throw new KzApiException("[Kz:applyPushToken] apply token failed, result:" + result);
        }

        JSONObject data = returnJson.getJSONObject("data");
        String clientId = data.getString("clientId");
        String token = data.getString("token");

        return ImmutableMap.of("clientId", clientId, "token", token);
    }

    /**
     * 通过社区登录功能验证微信服务号是否开启授权登录
     * @param siteId 用户的快站ID
     * @param openLogin 授权登录状态，0关闭1开启
     * @throws KzApiException 社区登录验证失败
     * */
    public static boolean updateKzAccountWxLogin(long siteId, boolean openLogin) throws KzApiException {
        //指定Host
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", ApplicationConfig.KZ_SERVICE_FORUM_HOST);

        //传递参数
        Map<String, Object> params = new HashMap<>();
        params.put("status", openLogin);

        String result = HttpClientUtil.post(KzApiConfig.getKzServiceAuthLoginConfigUrl(siteId), params, headers);
        if (result == null) {
            throw new KzApiException("[Kz:applyPushToken] result is null");
        }

        JSONObject resultJson = new JSONObject(result);
        int code = resultJson.optInt("code");

        if (code != 0) {
            throw new KzApiException("[Kz:kzAccountWxLoginCheck] Invalid Json result, result:" + result);
        }
        return true;
    }

    /**
     * 调用php微信回调接口
     * @return php处理的结果
     */
    public static String kzResponseMsg(String appId, String timestamp, String nonce, String xmlStr) {
        Map<String, Object> param =  new HashMap<>();
        param.put("app_id", appId);
        param.put("timestamp", timestamp);
        param.put("nonce", nonce);
        param.put("post_str", xmlStr);

        Map<String, String> header = ImmutableMap.of("Host", ApplicationConfig.KZ_SERVICE_HOST);

        String result = HttpClientUtil.post(KzApiConfig.KZ_OLD_WX_CALLBACK, param, header);
        if (result == null) {
            throw new KzApiException("[Kz:responseMsg] result is null");
        }

        JSONObject resultJson;
        try {
            resultJson = new JSONObject(result);
        } catch (JSONException e) {
            throw new KzApiException("[Kz:responseMsg] Json Parse Error, result:" + result + " param: " + param, e);
        }
        if (resultJson.optInt("ret") != 0) {
            throw new KzApiException("[Kz:responseMsg] unexpected result, result:" + result + " param: " + param);
        }
        return resultJson.getString("result");
    }

    /**
     * 调用php微信全网发布测试的接口
     * @return php处理的结果
     */
    public static String kzResponseTest(String timestamp, String nonce, String xmlStr) {
        Map<String, Object> param =  new HashMap<>();
        param.put("timestamp", timestamp);
        param.put("nonce", nonce);
        param.put("post_str", xmlStr);

        Map<String, String> header = ImmutableMap.of("Host", ApplicationConfig.KZ_SERVICE_HOST);

        String result = HttpClientUtil.post(KzApiConfig.KZ_OLD_WX_TEST_CALLBACK, param, header);
        if (result == null) {
            throw new KzApiException("[Kz:responseMsg] result is null");
        }

        JSONObject resultJson;
        try {
            resultJson = new JSONObject(result);
        } catch (JSONException e) {
            throw new KzApiException("[Kz:responseMsg] Json Parse Error, result:" + result);
        }
        if (resultJson.optInt("ret") != 0) {
            throw new KzApiException("[Kz:responseMsg] unexpected result, result:" + result);
        }
        return resultJson.getString("result");
    }
}
