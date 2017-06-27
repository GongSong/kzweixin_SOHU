package com.kuaizhan.kzweixin.controller;


import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.controller.param.TagNameParam;
import com.kuaizhan.kzweixin.controller.param.UpdateFanTagParam;
import com.kuaizhan.kzweixin.controller.param.UserBlacklistParam;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.controller.vo.FanListVO;
import com.kuaizhan.kzweixin.dao.po.auto.FanPO;
import com.kuaizhan.kzweixin.entity.common.Page;
import com.kuaizhan.kzweixin.entity.fan.TagDTO;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.FanService;
import org.springframework.web.bind.annotation.*;
import com.kuaizhan.kzweixin.utils.PojoSwitcher;

import javax.annotation.Resource;
import javax.validation.Valid;
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
    @Resource
    private AccountService accountService;

    /**
     * 创建标签
     * */
    @RequestMapping(value = "/fan/tags", method = RequestMethod.POST)
    public JsonResponse createTag(@Valid @RequestBody TagNameParam param) {
        int tagId = fansService.createTag(param.getWeixinAppid(), param.getTagName());
        return new JsonResponse(ImmutableMap.of("id", tagId, "name", param.getTagName()));
    }

    /**
     * 获取已创建的标签列表
     * */
    @RequestMapping(value = "/fan/tags", method = RequestMethod.GET)
    public JsonResponse getTags(@RequestParam long weixinAppid) {
        List<TagDTO> tagList = fansService.getTags(weixinAppid);
        return new JsonResponse(ImmutableMap.of("tags", tagList));
    }

    /**
     * 编辑（重命名）标签
     * */
    @RequestMapping(value = "/fan/tags/{tagId}", method = RequestMethod.PUT)
    public JsonResponse updateTag(@PathVariable("tagId") int tagId, @Valid @RequestBody TagNameParam param) {
        fansService.updateTag(param.getWeixinAppid(), tagId, param.getTagName());
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 删除标签
     * */
    @RequestMapping(value = "/fan/tags/{tagId}", method = RequestMethod.DELETE)
    public JsonResponse deleteTag(@PathVariable("tagId") int tagId, @RequestParam long weixinAppid) {
        fansService.deleteTag(weixinAppid, tagId);
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 给粉丝贴标签
     * */
    @RequestMapping(value = "/fan/fan_tags", method = RequestMethod.PUT)
    public JsonResponse updateFanTag(@Valid @RequestBody UpdateFanTagParam param) {
        fansService.addFanTag(param.getWeixinAppid(), param.getFansOpenId(), param.getNewTagsId());
        fansService.deleteFanTag(param.getWeixinAppid(), param.getFansOpenId(), param.getDeleteTagsId());
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 按标签搜索粉丝
     * */
    @RequestMapping(value = "/fan/fans", method = RequestMethod.GET)
    public JsonResponse fanTagSearch(@RequestParam long weixinAppid, @RequestParam int pageNum,
                                     @RequestParam(required = false) List<Integer> tagIds,
                                     @RequestParam(required = false) String queryStr,
                                     @RequestParam(required = false, defaultValue = "0") int isBlacklist) {
        Page<FanPO> fanPage = fansService.listFansByPage(weixinAppid, pageNum, AppConstant.PAGE_SIZE_LARGE, tagIds, queryStr, isBlacklist);
        List<FanPO> fanPOList = fanPage.getResult();

        FanListVO fanListVO = new FanListVO();
        fanListVO.setTotalNum(fanPage.getTotalCount());
        fanListVO.setTotalPage(fanPage.getTotalPages());
        fanListVO.setCurrentPage(fanPage.getPageNo());

        for(FanPO fanPO: fanPOList) {
            fanListVO.getFans().add(PojoSwitcher.fanPOToVO(fanPO));
        }
        return new JsonResponse(fanListVO);
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

}
