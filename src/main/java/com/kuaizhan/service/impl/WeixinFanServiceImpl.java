package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApiConfig;
import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.mapper.FanDao;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.XMLParseException;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DO.FanDO;
import com.kuaizhan.pojo.DTO.TagDTO;
import com.kuaizhan.service.WeixinFanService;
import com.kuaizhan.utils.HttpClientUtil;
import com.kuaizhan.utils.JsonUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liangjiateng on 2017/3/17.
 */
@Service("weixinFanService")
public class WeixinFanServiceImpl implements WeixinFanService {

    @Resource
    FanDao fanDao;

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
        List<String> openIdList = new ArrayList<>();
        for (FanDO fan : fanDOList) {
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
        List<String> openIdList = new ArrayList<>();
        for (FanDO fans : fanDOList) {
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

    @Override
    public FanDO getFan(String appId, String accessToken, String openId) {
        String returnJson = HttpClientUtil.get(ApiConfig.getFanInfoUrl(accessToken, openId));
        JSONObject jsonObject = new JSONObject(returnJson);
        if (jsonObject.has("errcode")) {
            return null;
        } else {
            FanDO fans = new FanDO();
            fans.setOpenId(jsonObject.getString("openid"));
            fans.setNickName(jsonObject.getString("nickname"));
            fans.setSex(jsonObject.getInt("sex"));
            fans.setLanguage(jsonObject.getString("language"));
            fans.setCity(jsonObject.getString("city"));
            fans.setProvince(jsonObject.getString("province"));
            fans.setCountry(jsonObject.getString("country"));
            fans.setHeadImgUrl(jsonObject.getString("headimgurl"));
            fans.setUnionId(jsonObject.getString("unionid"));
            fans.setSubscribeTime(jsonObject.getLong("subscribe_time"));
            fans.setGroupId(jsonObject.getLong("groupid"));
            fans.setAppId(appId);
            fans.setTagIdsJson(jsonObject.getJSONArray("tagid_list").toString());
            fans.setInBlackList(0);
            fans.setRemark(jsonObject.getString("remark"));
            fans.setStatus(1);
            fans.setLastInteractTime(0L);
            return fans;
        }
    }

    @Override
    public void subscribe(AccountDO accountDO, String msg) throws DaoException, XMLParseException {
        Document document;
        try {
            document = DocumentHelper.parseText(msg);
        } catch (Exception e) {
            throw new XMLParseException(e);
        }
        Element root = document.getRootElement();
        Element openId = root.element("FromUserName");
        List<String> tables = ApplicationConfig.getFanTableNames();
        FanDO fan;
        try {
            fan = fanDao.getDeleteFanByOpenId(openId.getText(), accountDO.getAppId(), tables);
        } catch (Exception e) {
            throw new DaoException(e);
        }
        //存在则更改status
        if (fan != null) {
            fan.setStatus(1);
            try {
                fanDao.updateFan(fan, tables);
            } catch (Exception e) {
                throw new DaoException(e);
            }
        }
        //不存在则创建新数据
        if (fan == null) {
            String tableName = ApplicationConfig.chooseFanTable(System.currentTimeMillis());
            FanDO newFans = getFan(accountDO.getAppId(), accountDO.getAccessToken(), openId.getText());
            try {
                fanDao.insertFan(newFans, tableName);
            } catch (Exception e) {
                throw new DaoException(e);
            }
        }
    }

    @Override
    public void unSubscribe(AccountDO accountDO, String msg) throws XMLParseException, DaoException {
        Document document;
        try {
            document = DocumentHelper.parseText(msg);
        } catch (Exception e) {
            throw new XMLParseException(e);
        }
        Element root = document.getRootElement();
        Element openId = root.element("FromUserName");
        List<String> tables = ApplicationConfig.getFanTableNames();
        try {
            FanDO fanDO = fanDao.getFanByOpenId(openId.getText(), accountDO.getAppId(), tables);
            if (fanDO != null) {
                fanDO.setStatus(2);
                fanDao.updateFan(fanDO, tables);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }

    }
}
