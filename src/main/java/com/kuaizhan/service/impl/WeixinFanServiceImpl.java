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

import java.io.IOException;
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
        return 0;
    }

    @Override
    public int deleteTag(String accessToken, int tagId) {
        return 0;
    }

    @Override
    public int renameTag(String accessToken, int tagId, String newName) {
        return 0;
    }

    @Override
    public int insertBlack(String accessToken, List<FanDO> fanDOList) {
        return 0;
    }

    @Override
    public int removeBlack(String accessToken, List<FanDO> fanDOList) {
        return 0;
    }
}
