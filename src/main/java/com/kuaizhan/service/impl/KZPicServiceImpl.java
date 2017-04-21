package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.exception.business.KZPicUploadException;
import com.kuaizhan.service.KZPicService;
import com.kuaizhan.utils.HttpClientUtil;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 快站图片服务
 *
 * Created by lorin on 17-3-30.
 */

@Service("kZPicService")
public class KZPicServiceImpl implements KZPicService {

    private static Logger logger = Logger.getLogger(KZPicServiceImpl.class);

    @Override
    public File download(String url, int timeout) {
        return null;
    }

    @Override
    public File download(String url) {
        return download(url, 4);
    }

    @Override
    public String uploadByPathAndUserId(String path, long userId) throws KZPicUploadException {
        return null;
    }

    @Override
    public String uploadByUrlAndUserId(String url, long userId) throws KZPicUploadException {
        Map<String, Object> params = new HashMap<>();
        params.put("img_url", url);
        params.put("uid", userId);
        // 指定host
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", ApplicationConfig.KZ_SERVICE_HOST);

        String result = HttpClientUtil.post(ApplicationConfig.getPicUploadUrl(), params, headers);

        JSONObject returnJson;
        try {
            returnJson = new JSONObject(result);
        }catch (JSONException e){
            String msg = "[上传图片到快站] 上传失败，url: " +  ApplicationConfig.getPicUploadUrl() + " param: " + params + "headers: " + headers + " result: " + result;
            throw new KZPicUploadException(msg);
        }

        if (returnJson.getInt("ret") == 0) {
            JSONObject data = returnJson.getJSONObject("data");
            return data.getString("url");
        } else {
            String msg = "[上传图片到快站] 上传失败，url: " +  ApplicationConfig.getPicUploadUrl() + " param: " + params + "headers: " + headers + " result: " + result;
            throw new KZPicUploadException(msg);
        }
    }
}
