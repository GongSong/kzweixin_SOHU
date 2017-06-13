package com.kuaizhan.kzweixin.controller;


import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.exception.deprecated.business.*;
import com.kuaizhan.kzweixin.exception.common.DaoException;
import com.kuaizhan.kzweixin.exception.deprecated.system.JsonParseException;
import com.kuaizhan.kzweixin.exception.common.RedisException;
import com.kuaizhan.kzweixin.exception.deprecated.system.ServerException;
import com.kuaizhan.kzweixin.pojo.po.AccountPO;
import com.kuaizhan.kzweixin.pojo.po.FanPO;
import com.kuaizhan.kzweixin.pojo.dto.Page;
import com.kuaizhan.kzweixin.pojo.dto.TagDTO;
import com.kuaizhan.kzweixin.controller.vo.FanListVO;
import com.kuaizhan.kzweixin.controller.vo.FanVO;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.FanService;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 粉丝模块接口
 * Created by Mr.Jadyn on 2016/12/29.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION, produces = "application/json")
public class FanController extends BaseController {

    @Resource
    private FanService fansService;
    @Resource
    private AccountService accountService;

    /**
     * 获取粉丝列表
     */
    @RequestMapping(value = "/fans", method = RequestMethod.GET)
    public JsonResponse listFanByPagination(@RequestParam long siteId, @RequestParam int page, @RequestParam(required = false) List<Integer> tagIds, @RequestParam int isBlack, @RequestParam(required = false) String keyword) throws RedisException, DaoException, ParamException, TagGetException, JsonParseException {
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);

        if (page < 1) {
            throw new ParamException();
        }
        String appId = accountPO.getAppId();
        String accessToken = accountPO.getAccessToken();

        Page<FanPO> fanDOPage = fansService.listFanByPagination(siteId, appId, page, isBlack, tagIds, keyword);
        List<FanPO> fanPOList = fanDOPage.getResult();
        FanListVO fanListVO = new FanListVO();
        if (fanPOList != null) {
            fanListVO.setTotalNum(fanDOPage.getTotalCount());
            fanListVO.setCurrentPage(fanDOPage.getPageNo());
            fanListVO.setTotalPage(fanDOPage.getTotalPages());
            List<TagDTO> tags = fansService.listTags(siteId, accessToken);
            for (FanPO fanPO : fanPOList) {
                FanVO fanVO = new FanVO();
                fanVO.setId(fanPO.getFanId());
                fanVO.setName(fanPO.getNickName());
                fanVO.setAddress(fanPO.getCountry() + " " + fanPO.getProvince() + " " + fanPO.getCity());
                fanVO.setSex(fanPO.getSex());
                fanVO.setAvatar(fanPO.getHeadImgUrl());
                fanVO.setFocusTime(fanPO.getSubscribeTime());
                fanVO.setOpenId(fanPO.getOpenId());
                List<String> userTags = new ArrayList<>();
                if (tags != null) {
                    for (TagDTO tag : tags) {
                        if (fanPO.getTagIdsJson().contains(tag.getId() + "")) {
                            userTags.add(tag.getName());
                        }
                    }
                }
                fanVO.setTags(userTags);
                fanListVO.getFans().add(fanVO);
            }
        }
        return new JsonResponse(fanListVO);
    }

    /**
     * 获取所有标签
     */
    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public JsonResponse listTags(@RequestParam long siteId) throws RedisException, DaoException, TagGetException, JsonParseException {
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        List<TagDTO> list = fansService.listTags(siteId, accountPO.getAccessToken());
        return new JsonResponse(list);
    }

    /**
     * 创建标签
     */
    @RequestMapping(value = "/tags", method = RequestMethod.POST)
    public JsonResponse insertTag(@RequestParam long siteId, @RequestBody String postData) throws RedisException, DaoException, ParamException, TagDuplicateNameException, TagNameLengthException, TagNumberException, ServerException, JsonParseException {
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        String tagName;
        try {
            JSONObject jsonObject = new JSONObject(postData);
            tagName = jsonObject.getString("tagName");
        } catch (Exception e) {
            throw new ParamException();
        }
        fansService.insertTag(siteId, tagName, accountPO.getAccessToken());
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 更新用户标签
     */
    @RequestMapping(value = "/fans/tag", method = RequestMethod.PUT)
    public JsonResponse updateUserTag(@RequestParam long siteId, @RequestBody String postData) throws RedisException, DaoException, ParamException, OpenIdNumberException, OpenIdException, FanTagNumberException, TagException, ServerException, JsonParseException {
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        List<String> openIds;
        List<Integer> tagIds;
        try {
            JSONObject jsonObject = new JSONObject(postData);
            openIds = JsonUtil.string2List(jsonObject.get("openIds").toString(), String.class);
            tagIds = JsonUtil.string2List(jsonObject.get("tagIds").toString(), Integer.class);
        } catch (Exception e) {
            throw new ParamException();
        }
        fansService.updateUserTag(siteId, accountPO.getAppId(), openIds, tagIds, accountPO.getAccessToken());
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 删除标签
     */
    @RequestMapping(value = "/tags/{tagId}", method = RequestMethod.DELETE)
    public JsonResponse deleteTag(@RequestParam long siteId, @PathVariable int tagId) throws RedisException, DaoException, ServerException, TagDeleteFansNumberException, TagModifyException, JsonParseException {
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        fansService.deleteTag(siteId, accountPO.getAppId(), tagId, accountPO.getAccessToken());
        return new JsonResponse(ImmutableMap.of());

    }

    /**
     * 修改标签
     */
    @RequestMapping(value = "/tags", method = RequestMethod.PUT)
    public JsonResponse renameTag(@RequestParam long siteId, @RequestBody String postData) throws RedisException, DaoException, ParamException, TagDuplicateNameException, TagNameLengthException, TagModifyException, ServerException, JsonParseException {
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        int tagId;
        String newName;
        try {
            JSONObject jsonObject = new JSONObject(postData);
            tagId = jsonObject.getInt("tagId");
            newName = jsonObject.getString("newName");
        } catch (Exception e) {
            throw new ParamException();
        }
        TagDTO tag = new TagDTO();
        tag.setId(tagId);
        tag.setName(newName);
        fansService.renameTag(siteId, tag, accountPO.getAccessToken());
        return new JsonResponse(ImmutableMap.of());

    }

    /**
     * 将用户加入黑名单
     */
    @RequestMapping(value = "/fans/black", method = RequestMethod.POST)
    public JsonResponse insertBlack(@RequestParam long siteId, @RequestBody String postData) throws RedisException, DaoException, ParamException, ServerException, BlackAddNumberException, OpenIdException, JsonParseException {
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        List<Long> fanIds;
        List<String> openIds;
        try {
            JSONObject jsonObject = new JSONObject(postData);
            fanIds = JsonUtil.string2List(jsonObject.get("fanIds").toString(), Long.class);
            openIds = JsonUtil.string2List(jsonObject.get("openIds").toString(), String.class);
        } catch (Exception e) {
            throw new ParamException();
        }
        List<FanPO> fanPOList = new ArrayList<>();
        for (int i = 0; i < fanIds.size(); i++) {
            FanPO fan = new FanPO();
            fan.setFanId(fanIds.get(i));
            fan.setOpenId(openIds.get(i));
            fanPOList.add(fan);
        }
        fansService.insertBlack(siteId, accountPO.getAccessToken(), fanPOList);
        return new JsonResponse(ImmutableMap.of());

    }

    /**
     * 将用户移除黑名单
     */
    @RequestMapping(value = "/fans/black", method = RequestMethod.DELETE)
    public JsonResponse deleteBlack(@RequestParam long siteId, @RequestBody String postData) throws RedisException, DaoException, ParamException, ServerException, BlackAddNumberException, OpenIdException, JsonParseException {
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        List<FanPO> fanPOList = new ArrayList<>();
        List<Long> fanIds;
        List<String> openIds;
        try {
            JSONObject jsonObject = new JSONObject(postData);
            openIds = JsonUtil.string2List(jsonObject.get("openIds").toString(), String.class);
            fanIds = JsonUtil.string2List(jsonObject.get("fanIds").toString(), Long.class);
        } catch (Exception e) {
            throw new ParamException();
        }
        for (int i = 0; i < fanIds.size(); i++) {
            FanPO fans = new FanPO();
            fans.setOpenId(openIds.get(i));
            fans.setFanId(fanIds.get(i));
            fanPOList.add(fans);
        }
        fansService.deleteBlack(siteId, fanPOList, accountPO.getAccessToken());
        return new JsonResponse(ImmutableMap.of());

    }
}
