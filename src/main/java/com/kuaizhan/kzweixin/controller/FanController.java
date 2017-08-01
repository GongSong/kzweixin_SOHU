package com.kuaizhan.kzweixin.controller;


import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.controller.param.TagNameParam;
import com.kuaizhan.kzweixin.controller.param.UpdateFanTagParam;
import com.kuaizhan.kzweixin.controller.param.UserBlacklistParam;
import com.kuaizhan.kzweixin.controller.vo.FanVO;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.dao.po.auto.FanPO;
import com.kuaizhan.kzweixin.entity.common.PageV2;
import com.kuaizhan.kzweixin.entity.fan.TagDTO;
import com.kuaizhan.kzweixin.service.FanService;
import com.kuaizhan.kzweixin.utils.ParamUtil;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.web.bind.annotation.*;
import com.kuaizhan.kzweixin.utils.PojoSwitcher;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * 粉丝模块接口
 * Created by fangtianyu on 6/15/17.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION, produces = "application/json")
public class FanController extends BaseController {

    @Resource
    private FanService fansService;

    /**
     * 创建标签
     */
    @RequestMapping(value = "/fan/tags", method = RequestMethod.POST)
    public JsonResponse createTag(@Valid @RequestBody TagNameParam param) {
        int tagId = fansService.createTag(param.getWeixinAppid(), param.getTagName());
        return new JsonResponse(ImmutableMap.of("id", tagId, "name", param.getTagName()));
    }

    /**
     * 获取已创建的标签列表
     */
    @RequestMapping(value = "/fan/tags", method = RequestMethod.GET)
    public JsonResponse getTags(@RequestParam long weixinAppid) {
        List<TagDTO> tagList = fansService.getTags(weixinAppid);
        return new JsonResponse(ImmutableMap.of("tags", tagList));
    }

    /**
     * 编辑（重命名）标签
     */
    @RequestMapping(value = "/fan/tags/{tagId}", method = RequestMethod.PUT)
    public JsonResponse updateTag(@PathVariable("tagId") int tagId, @Valid @RequestBody TagNameParam param) {
        fansService.updateTag(param.getWeixinAppid(), tagId, param.getTagName());
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 删除标签
     */
    @RequestMapping(value = "/fan/tags/{tagId}", method = RequestMethod.DELETE)
    public JsonResponse deleteTag(@PathVariable("tagId") int tagId, @RequestParam long weixinAppid) {
        fansService.deleteTag(weixinAppid, tagId);
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 给粉丝贴标签
     */
    @RequestMapping(value = "/fan/fan_tags", method = RequestMethod.PUT)
    public JsonResponse updateFanTag(@Valid @RequestBody UpdateFanTagParam param) {
        fansService.addFanTag(param.getWeixinAppid(), param.getOpenIds(), param.getNewTagIds());
        fansService.deleteFanTag(param.getWeixinAppid(), param.getOpenIds(), param.getDeleteTagIds());
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 按标签搜索粉丝
     */
    @RequestMapping(value = "/fans", method = RequestMethod.GET)
    public JsonResponse listFansByPage(@RequestParam long weixinAppid,
                                     @RequestParam(defaultValue = "0") int offset,
                                     @RequestParam(defaultValue = "20") int limit,
                                     @RequestParam(required = false, name = "tagIds") String tagIdsStr,
                                     @RequestParam(required = false) String queryStr,
                                     @RequestParam(defaultValue = "false") Boolean isBlacklist,
                                     @RequestParam(defaultValue = "false") Boolean hasInteract) {
        // 前端node转发不方便传list，遂通过Json字符串来传
        List<Integer> tagIds = ParamUtil.getIntList(tagIdsStr);

        PageV2<FanPO> fanPage = fansService.listFansByPage(weixinAppid, offset, limit, tagIds, queryStr, isBlacklist, hasInteract);
        List<FanVO> fanVOList = new ArrayList<>();

        for(FanPO fanPO: fanPage.getDataSet()) {
            fanVOList.add(PojoSwitcher.fanPOToVO(fanPO));
        }
        return new JsonResponse(ImmutableMap.of("total", fanPage.getTotal(), "fans", fanVOList));
    }

    /**
     * 更新黑名单用户信息
     * */
    @RequestMapping(value = "/fan/blacklist", method = RequestMethod.PUT)
    public JsonResponse updateFanBlacklist(@Valid @RequestBody UserBlacklistParam param) {
        if (param.getSetBlacklist()) {
            fansService.addFanBlacklist(param.getWeixinAppid(), param.getOpenIds());
        } else {
            fansService.removeFanBlacklist(param.getWeixinAppid(), param.getOpenIds());
        }
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 根据openId判断粉丝是否关注
     */
    @RequestMapping(value = "/fans/{openId}/is_subscribe", method = RequestMethod.GET)
    public JsonResponse isSubscribe(@PathVariable String openId,
                                    @RequestParam String appId) {
        boolean isSubscribe = fansService.isSubscribe(appId, openId);
        return new JsonResponse(ImmutableMap.of("isSubscribe", isSubscribe));
    }

    /**
     * 根据头像和昵称判断粉丝是否关注
     */
    @RequestMapping(value = "/fan/is_subscribe", method = RequestMethod.GET)
    public JsonResponse isSubscribeByInfo(@RequestParam String appId,
                                          @RequestParam String nickName,
                                          @RequestParam String headImgUrl) {
        // 先全部返回false, 待日后实现
        return new JsonResponse(ImmutableMap.of("isSubscribe", false));
    }
}
