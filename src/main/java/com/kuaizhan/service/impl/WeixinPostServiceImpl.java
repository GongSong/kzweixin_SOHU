package com.kuaizhan.service.impl;


import com.kuaizhan.config.ApiConfig;
import com.kuaizhan.exception.business.MaterialDeleteException;
import com.kuaizhan.service.WeixinPostService;
import com.kuaizhan.utils.HttpClientUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Service;


/**
 * Created by liangjiateng on 2017/3/28.
 */
@Service("weixinPostService")
public class WeixinPostServiceImpl implements WeixinPostService {


    @Override
    public void deletePost(String mediaId, String accessToken) throws MaterialDeleteException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("media_id", mediaId);
        String result = HttpClientUtil.postJson(ApiConfig.deleteMaterialUrl(accessToken), mediaId);
        JSONObject returnJson = new JSONObject(result);
        if (returnJson.getInt("errcode") != 0) {
            throw new MaterialDeleteException();
        }
    }
}
