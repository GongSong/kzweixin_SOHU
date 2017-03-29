package com.kuaizhan.service.impl;


import com.kuaizhan.config.ApiConfig;
import com.kuaizhan.exception.business.AddMaterialException;
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
        String result = HttpClientUtil.postJson(ApiConfig.deleteMaterialUrl(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);
        if (returnJson.getInt("errcode") != 0) {
            throw new MaterialDeleteException();
        }
    }

    @Override
    public String[] uploadImage(String accessToken, String imgUrl) throws AddMaterialException {
        String result = HttpClientUtil.postMedia(ApiConfig.addMaterialUrl(accessToken, "image"), imgUrl);
        JSONObject returnJson = new JSONObject(result);
        if (returnJson.has("errcode")) {
            throw new AddMaterialException();
        }
        String[] str = new String[2];
        str[0] = returnJson.getString("media_id");
        str[1] = returnJson.getString("url");
        return str;
    }
}
