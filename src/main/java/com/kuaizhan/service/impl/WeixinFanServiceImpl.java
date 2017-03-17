package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApiConfig;
import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.pojo.DO.FanDO;
import com.kuaizhan.pojo.DTO.TagDTO;
import com.kuaizhan.service.WeixinFanService;
import com.kuaizhan.utils.HttpClientUtil;
import com.kuaizhan.utils.JsonUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liangjiateng on 2017/3/17.
 */
@Service("weixinFanService")
public class WeixinFanServiceImpl implements WeixinFanService {
    @Override
    public List<TagDTO> listTags(String accessToken) throws Exception {
        String returnJson = HttpClientUtil.get(ApiConfig.getTagsUrl(accessToken));
        JSONObject jsonObject = new JSONObject(returnJson);
        String tagsJson = jsonObject.get("tags").toString();
        List<TagDTO> tags = JsonUtil.string2List(tagsJson, TagDTO.class);
        return tags;
    }

    @Override
    public int insertTag(String accessToken, String tagName) {
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> map = new HashMap<>();
        map.put("name", tagName);
        jsonObject.put("tag", map);
        String result = HttpClientUtil.postJson(ApiConfig.getCreateTagsUrl(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);
        if (returnJson.has("tag"))
            return 1;
        else
            return Integer.parseInt(new JSONObject(result).get("errcode").toString());
    }

    @Override
    public int updateTag(String accessToken, List<String> openIdList, int tagId) {
        JSONObject jsonObject = new JSONObject();
        if (openIdList != null)
            jsonObject.put("openid_list", openIdList);
        jsonObject.put("tagid", tagId);
        String result = HttpClientUtil.postJson(ApiConfig.setTagsUrl(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);
        if (Integer.parseInt(returnJson.get("errcode").toString()) == 0)
            return 1;
        else
            return Integer.parseInt(returnJson.get("errcode").toString());
    }

    @Override
    public int deleteTag(String accessToken, int tagId) {
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> id = new HashMap<>();
        id.put("id", tagId);
        jsonObject.put("tag", id);
        String result = HttpClientUtil.postJson(ApiConfig.deleteTagsUrl(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);
        if (Integer.parseInt(returnJson.get("errcode").toString()) == 0)
            return 1;
        else
            return Integer.parseInt(returnJson.get("errcode").toString());
    }

    @Override
    public int renameTag(String accessToken, int tagId, String newName) {
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> data = new HashMap<>();
        data.put("id", tagId);
        data.put("name", newName);
        jsonObject.put("tag", data);
        String result = HttpClientUtil.postJson(ApiConfig.updateTagsUrl(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);
        if (Integer.parseInt(returnJson.get("errcode").toString()) == 0)
            return 1;
        else
            return Integer.parseInt(returnJson.get("errcode").toString());
    }

    @Override
    public int insertBlack(String accessToken, List<FanDO> fanDOList) {
        List<String> openIdList=new ArrayList<>();
        for(FanDO fan:fanDOList){
            openIdList.add(fan.getOpenId());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openid_list", openIdList);
        String result = HttpClientUtil.postJson(ApiConfig.insertBlackUrl(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);
        if (Integer.parseInt(returnJson.get("errcode").toString()) == 0)
            return 1;
        else
            return Integer.parseInt(returnJson.get("errcode").toString());
    }

    @Override
    public int removeBlack(String accessToken, List<FanDO> fanDOList) {
        List<String> openIdList=new ArrayList<>();
        for(FanDO fans:fanDOList){
            openIdList.add(fans.getOpenId());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openid_list", openIdList);
        String result = HttpClientUtil.postJson(ApiConfig.deleteBlackUrl(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);
        if (Integer.parseInt(returnJson.get("errcode").toString()) == 0)
            return 1;
        else
            return Integer.parseInt(returnJson.get("errcode").toString());
    }
}
