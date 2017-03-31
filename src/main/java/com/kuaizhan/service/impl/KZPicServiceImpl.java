package com.kuaizhan.service.impl;

import com.kuaizhan.exception.business.KZPicUploadException;
import com.kuaizhan.service.KZPicService;
import com.kuaizhan.utils.HttpClientUtil;
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
        String tempPicApi = "http://service.kuaizhan.sohuno.com/pic/servcie-upload-pic-by-url";
        Map<String, Object> params = new HashMap<>();
        params.put("img_url", url);
        params.put("uid", userId);
        String result = HttpClientUtil.post(tempPicApi, params);
        JSONObject returnJson = new JSONObject(result);
        if (returnJson.getInt("ret") == 0) {
            return returnJson.getString("url");
        } else {
            throw new KZPicUploadException();
        }
    }
}
