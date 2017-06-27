package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.config.WxApiConfig;
import com.kuaizhan.kzweixin.dao.mapper.FanDao;
import com.kuaizhan.kzweixin.exception.common.DaoException;
import com.kuaizhan.kzweixin.exception.common.XMLParseException;
import com.kuaizhan.kzweixin.dao.po.auto.AccountPO;
import com.kuaizhan.kzweixin.dao.po.auto.FanPO;
import com.kuaizhan.kzweixin.entity.fan.TagDTO;
import com.kuaizhan.kzweixin.service.WeixinFanService;
import com.kuaizhan.kzweixin.utils.DBTableUtil;
import com.kuaizhan.kzweixin.utils.HttpClientUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
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
        String returnJson = HttpClientUtil.get(WxApiConfig.getTagsUrl(accessToken));
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
        String result = HttpClientUtil.postJson(WxApiConfig.getCreateTagsUrl(accessToken), jsonObject.toString());
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
        String result = HttpClientUtil.postJson(WxApiConfig.setUserTagUrl(accessToken), jsonObject.toString());
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
        String result = HttpClientUtil.postJson(WxApiConfig.deleteTagUrl(accessToken), jsonObject.toString());
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
        String result = HttpClientUtil.postJson(WxApiConfig.updateTagsUrl(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);
        if (Integer.parseInt(returnJson.get("errcode").toString()) == 0)
            return 1;
        else
            return Integer.parseInt(returnJson.get("errcode").toString());
    }

    @Override
    public int insertBlack(String accessToken, List<FanPO> fanPOList) {
        List<String> openIdList = new ArrayList<>();
        for (FanPO fan : fanPOList) {
            openIdList.add(fan.getOpenId());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openid_list", openIdList);
        String result = HttpClientUtil.postJson(WxApiConfig.insertBlackUrl(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);
        if (Integer.parseInt(returnJson.get("errcode").toString()) == 0)
            return 1;
        else
            return Integer.parseInt(returnJson.get("errcode").toString());
    }

    @Override
    public int removeBlack(String accessToken, List<FanPO> fanPOList) {
        List<String> openIdList = new ArrayList<>();
        for (FanPO fans : fanPOList) {
            openIdList.add(fans.getOpenId());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openid_list", openIdList);
        String result = HttpClientUtil.postJson(WxApiConfig.deleteBlackUrl(accessToken), jsonObject.toString());
        JSONObject returnJson = new JSONObject(result);
        if (Integer.parseInt(returnJson.get("errcode").toString()) == 0)
            return 1;
        else
            return Integer.parseInt(returnJson.get("errcode").toString());
    }

    @Override
    public FanPO getFan(String appId, String accessToken, String openId) {
        String returnJson = HttpClientUtil.get(WxApiConfig.getFanInfoUrl(accessToken, openId));
        JSONObject jsonObject = new JSONObject(returnJson);
        if (jsonObject.has("errcode")) {
            return null;
        } else {
            FanPO fans = new FanPO();
            fans.setOpenId(jsonObject.getString("openid"));
            fans.setNickName(jsonObject.getString("nickname"));
            fans.setSex(jsonObject.getInt("sex"));
            fans.setLanguage(jsonObject.getString("language"));
            fans.setCity(jsonObject.getString("city"));
            fans.setProvince(jsonObject.getString("province"));
            fans.setCountry(jsonObject.getString("country"));
            fans.setHeadImgUrl(jsonObject.getString("headimgurl"));
            fans.setUnionId(jsonObject.getString("unionid"));
            fans.setSubscribeTime(jsonObject.getInt("subscribe_time"));
            fans.setGroupId(jsonObject.getInt("groupid"));
            fans.setAppId(appId);
            fans.setTagIdsJson(jsonObject.getJSONArray("tagid_list").toString());
            fans.setInBlacklist(0);
            fans.setRemark(jsonObject.getString("remark"));
            fans.setStatus(1);
            fans.setLastInteractTime(0);
            return fans;
        }
    }

    @Override
    public void subscribe(AccountPO accountPO, String msg) throws DaoException, XMLParseException {
        Document document;
        try {
            document = DocumentHelper.parseText(msg);
        } catch (Exception e) {
            throw new XMLParseException(e);
        }
        Element root = document.getRootElement();
        Element openId = root.element("FromUserName");
        List<String> tables = DBTableUtil.getFanTableNames();
        FanPO fan;
        try {
            fan = fanDao.getDeleteFanByOpenId(openId.getText(), accountPO.getAppId(), tables);
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
            String tableName = DBTableUtil.chooseFanTable(System.currentTimeMillis());
            FanPO newFans = getFan(accountPO.getAppId(), accountPO.getAccessToken(), openId.getText());
            try {
                fanDao.insertFan(newFans, tableName);
            } catch (Exception e) {
                throw new DaoException(e);
            }
        }
    }

    @Override
    public void unSubscribe(AccountPO accountPO, String msg) throws XMLParseException, DaoException {
        Document document;
        try {
            document = DocumentHelper.parseText(msg);
        } catch (Exception e) {
            throw new XMLParseException(e);
        }
        Element root = document.getRootElement();
        Element openId = root.element("FromUserName");
        List<String> tables = DBTableUtil.getFanTableNames();
        String tableName = DBTableUtil.getFanTableName(accountPO.getAppId());
        try {
            FanPO fanPO = fanDao.getFanByOpenId(openId.getText(), accountPO.getAppId(), tableName);
            if (fanPO != null) {
                fanPO.setStatus(2);
                fanDao.updateFan(fanPO, tables);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }

    }
}
