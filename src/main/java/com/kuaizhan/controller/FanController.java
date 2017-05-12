package com.kuaizhan.controller;


import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.exception.deprecated.business.*;
import com.kuaizhan.exception.common.DaoException;
import com.kuaizhan.exception.deprecated.system.JsonParseException;
import com.kuaizhan.exception.common.RedisException;
import com.kuaizhan.exception.deprecated.system.ServerException;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DO.FanDO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.pojo.DTO.TagDTO;
import com.kuaizhan.pojo.VO.FanListVO;
import com.kuaizhan.pojo.VO.FanVO;
import com.kuaizhan.pojo.VO.JsonResponse;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.FanService;
import com.kuaizhan.utils.JsonUtil;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 粉丝管理Controller
 * Created by Mr.Jadyn on 2016/12/29.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION, produces = "application/json")
public class FanController extends BaseController {

    @Resource
    FanService fansService;
    @Resource
    AccountService accountService;

    /**
     * 获取粉丝列表
     *
     * @return
     */
    @RequestMapping(value = "/fans", method = RequestMethod.GET)
    public JsonResponse listFanByPagination(@RequestParam long siteId, @RequestParam int page, @RequestParam(required = false) List<Integer> tagIds, @RequestParam int isBlack, @RequestParam(required = false) String keyword) throws RedisException, DaoException, ParamException, TagGetException, JsonParseException {
        AccountDO accountDO = accountService.getAccountBySiteId(siteId);

        if (page < 1) {
            throw new ParamException();
        }
        String appId = accountDO.getAppId();
        String accessToken = accountDO.getAccessToken();

        Page<FanDO> fanDOPage = fansService.listFanByPagination(siteId, appId, page, isBlack, tagIds, keyword);
        List<FanDO> fanDOList = fanDOPage.getResult();
        FanListVO fanListVO = new FanListVO();
        if (fanDOList != null) {
            fanListVO.setTotalNum(fanDOPage.getTotalCount());
            fanListVO.setCurrentPage(fanDOPage.getPageNo());
            fanListVO.setTotalPage(fanDOPage.getTotalPages());
            List<TagDTO> tags = fansService.listTags(siteId, accessToken);
            for (FanDO fanDO : fanDOList) {
                FanVO fanVO = new FanVO();
                fanVO.setId(fanDO.getFanId());
                fanVO.setName(fanDO.getNickName());
                fanVO.setAddress(fanDO.getCountry() + " " + fanDO.getProvince() + " " + fanDO.getCity());
                fanVO.setSex(fanDO.getSex());
                fanVO.setAvatar(fanDO.getHeadImgUrl());
                fanVO.setFocusTime(fanDO.getSubscribeTime());
                fanVO.setOpenId(fanDO.getOpenId());
                List<String> userTags = new ArrayList<>();
                if (tags != null) {
                    for (TagDTO tag : tags) {
                        if (fanDO.getTagIdsJson().contains(tag.getId() + "")) {
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
     *
     * @param siteId 站点Id
     * @return
     */
    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public JsonResponse listTags(@RequestParam long siteId) throws RedisException, DaoException, TagGetException, JsonParseException {
        AccountDO accountDO = accountService.getAccountBySiteId(siteId);
        List<TagDTO> list = fansService.listTags(siteId, accountDO.getAccessToken());
        return new JsonResponse(list);
    }

    /**
     * 创建标签
     *
     * @param siteId 站点id
     * @return
     */
    @RequestMapping(value = "/tags", method = RequestMethod.POST)
    public JsonResponse insertTag(@RequestParam long siteId, @RequestBody String postData) throws RedisException, DaoException, ParamException, TagDuplicateNameException, TagNameLengthException, TagNumberException, ServerException, JsonParseException {
        AccountDO accountDO = accountService.getAccountBySiteId(siteId);
        String tagName;
        try {
            JSONObject jsonObject = new JSONObject(postData);
            tagName = jsonObject.getString("tagName");
        } catch (Exception e) {
            throw new ParamException();
        }
        fansService.insertTag(siteId, tagName, accountDO.getAccessToken());
        return new JsonResponse(null);
    }

    /**
     * 更新用户标签
     *
     * @param siteId
     * @param postData
     * @return
     */
    @RequestMapping(value = "/fans/tag", method = RequestMethod.PUT)
    public JsonResponse updateUserTag(@RequestParam long siteId, @RequestBody String postData) throws RedisException, DaoException, ParamException, OpenIdNumberException, OpenIdException, FanTagNumberException, TagException, ServerException, JsonParseException {
        AccountDO accountDO = accountService.getAccountBySiteId(siteId);
        List<String> openIds;
        List<Integer> tagIds;
        try {
            JSONObject jsonObject = new JSONObject(postData);
            openIds = JsonUtil.string2List(jsonObject.get("openIds").toString(), String.class);
            tagIds = JsonUtil.string2List(jsonObject.get("tagIds").toString(), Integer.class);
        } catch (Exception e) {
            throw new ParamException();
        }
        fansService.updateUserTag(siteId, accountDO.getAppId(), openIds, tagIds, accountDO.getAccessToken());
        return new JsonResponse(null);
    }

    /**
     * 删除标签
     *
     * @param siteId 站点id
     * @param tagId  标签id
     * @return
     */
    @RequestMapping(value = "/tags/{tagId}", method = RequestMethod.DELETE)
    public JsonResponse deleteTag(@RequestParam long siteId, @PathVariable int tagId) throws RedisException, DaoException, ServerException, TagDeleteFansNumberException, TagModifyException, JsonParseException {
        AccountDO accountDO = accountService.getAccountBySiteId(siteId);
        fansService.deleteTag(siteId, accountDO.getAppId(), tagId, accountDO.getAccessToken());
        return new JsonResponse(null);

    }

    /**
     * 修改标签
     *
     * @param siteId 站点id
     * @return
     */
    @RequestMapping(value = "/tags", method = RequestMethod.PUT)
    public JsonResponse renameTag(@RequestParam long siteId, @RequestBody String postData) throws RedisException, DaoException, ParamException, TagDuplicateNameException, TagNameLengthException, TagModifyException, ServerException, JsonParseException {
        AccountDO accountDO = accountService.getAccountBySiteId(siteId);
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
        fansService.renameTag(siteId, tag, accountDO.getAccessToken());
        return new JsonResponse(null);

    }

    /**
     * 将用户加入黑名单
     *
     * @param siteId 站点id
     * @return
     */
    @RequestMapping(value = "/fans/black", method = RequestMethod.POST)
    public JsonResponse insertBlack(@RequestParam long siteId, @RequestBody String postData) throws RedisException, DaoException, ParamException, ServerException, BlackAddNumberException, OpenIdException, JsonParseException {
        AccountDO accountDO = accountService.getAccountBySiteId(siteId);
        List<Long> fanIds;
        List<String> openIds;
        try {
            JSONObject jsonObject = new JSONObject(postData);
            fanIds = JsonUtil.string2List(jsonObject.get("fanIds").toString(), Long.class);
            openIds = JsonUtil.string2List(jsonObject.get("openIds").toString(), String.class);
        } catch (Exception e) {
            throw new ParamException();
        }
        List<FanDO> fanDOList = new ArrayList<>();
        for (int i = 0; i < fanIds.size(); i++) {
            FanDO fan = new FanDO();
            fan.setFanId(fanIds.get(i));
            fan.setOpenId(openIds.get(i));
            fanDOList.add(fan);
        }
        fansService.insertBlack(siteId, accountDO.getAccessToken(), fanDOList);
        return new JsonResponse(null);

    }

    /**
     * 将用户移除黑名单
     *
     * @param siteId 站点id
     * @return
     */
    @RequestMapping(value = "/fans/black", method = RequestMethod.DELETE)
    public JsonResponse deleteBlack(@RequestParam long siteId, @RequestBody String postData) throws RedisException, DaoException, ParamException, ServerException, BlackAddNumberException, OpenIdException, JsonParseException {
        AccountDO accountDO = accountService.getAccountBySiteId(siteId);
        List<FanDO> fanDOList = new ArrayList<>();
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
            FanDO fans = new FanDO();
            fans.setOpenId(openIds.get(i));
            fans.setFanId(fanIds.get(i));
            fanDOList.add(fans);
        }
        fansService.deleteBlack(siteId, fanDOList, accountDO.getAccessToken());
        return new JsonResponse(null);

    }
}


