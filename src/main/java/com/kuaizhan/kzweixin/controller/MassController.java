package com.kuaizhan.kzweixin.controller;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.dao.po.MassRespPO;
import com.kuaizhan.kzweixin.dao.po.PostPO;
import com.kuaizhan.kzweixin.dao.po.auto.AccountPO;
import com.kuaizhan.kzweixin.dao.po.auto.MassPO;
import com.kuaizhan.kzweixin.entity.fan.TagDTO;
import com.kuaizhan.kzweixin.entity.msg.CustomMsg;
import com.kuaizhan.kzweixin.enums.MsgType;
import com.kuaizhan.kzweixin.service.*;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 消息群发
 * Created by chen on 17/7/7.
 */

@RestController
@RequestMapping(value = AppConstant.VERSION, produces = "application/json")
public class MassController extends BaseController {

    @Resource
    protected MassService massService;

    @Resource
    protected PostService postService;

    @Resource
    protected AccountService accountService;

    @Resource
    protected FanService fanService;

    @Resource
    protected MsgService msgService;

    /**
     * 是否有多图文 @url: mass
     * @param massId
     * @return
     */
    @RequestMapping(value = "/mass/hasMulti/{massId}", method = RequestMethod.GET)
    public JsonResponse hasMulti (@PathVariable long massId) {
        MassPO massPO = massService.getMassById(massId);
        if(massPO.getResponseType() == MassPO.RespType.ARTICLE_LIST.getCode()) {
            List<MassRespPO> massRespPOS = JsonUtil.string2List(massPO.getResponseJson(), MassRespPO.class);
            if(massRespPOS != null && massRespPOS.size() > 0) {
                PostPO postPO = postService.getPostByPageId(massRespPOS.get(0).getPostId());
                if(postPO != null && postPO.getType() == PostPO.Type.Multi_One.getCode()) {
                    return new JsonResponse(true);
                }
            }
        }
        return new JsonResponse(false);
    }

    /**
     * 群发列表 @url: ajax-mass-msg-list
     * @param siteId
     * @param pageNO
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/mass/list", method = RequestMethod.POST)
    public JsonResponse getMassList(@RequestParam int siteId,
                                    @RequestParam(value = "pageNO", defaultValue = "1") int pageNO,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        List<MassPO> massPOList = null;
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        if(accountPO != null && accountPO.getWeixinAppid() != 0) {
            massPOList = massService.getMassByWxAppId(accountPO.getWeixinAppid());
        }
        return new JsonResponse(massPOList);
    }

    /**
     * 获取群发对象tag分组 @url: ajax-mass-groups-get
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/mass/tags/list", method = RequestMethod.POST)
    public JsonResponse getMassGroup(@RequestParam int siteId) {

        List<TagDTO> tagDTOList = null;
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        if(accountPO != null && accountPO.getWeixinAppid() != 0) {
            tagDTOList = fanService.getTags(accountPO.getWeixinAppid());
        }
        return new JsonResponse(tagDTOList);
    }

    /**
     * 消息预览&群发 @url: ajax-mass-msg-new
     * @param siteId 站点id
     * @param respType 1文章列表2页面3文字4图片
     * @param respJson 要发送的post, json, 兼容旧接口
     * @param postIds 逗号分隔
     * @param tagId 发送的目的标签id
     * @param publishTime 发布时间，定时群发时才有意义
     * @param isPreview 1表示预览 0表示群发
     * @param isTiming 1表示定时发布，0不定时
     * @param hasMulti 是否多图文
     * @param massId 0
     * @return
     */
    @RequestMapping(value = "/mass/msg/send", method = RequestMethod.POST)
    public JsonResponse sendMassMsg(@RequestParam int siteId,
                                     @RequestParam(value = "response_type") int respType,
                                     @RequestParam(value = "post_ids") String postIds,
                                     @RequestParam(value = "response_json", required = false, defaultValue = "") String respJson,
                                     @RequestParam(value = "tag_id") int tagId,
                                     @RequestParam(value = "publish_time") long publishTime,
                                     @RequestParam(value = "preview_or_not", defaultValue = "1") int isPreview,
                                     @RequestParam(value = "is_timing", defaultValue = "0") int isTiming,
                                     @RequestParam(value = "has_multi", defaultValue = "0") int hasMulti,
                                     @RequestParam(value = "mass_id", required = false, defaultValue = "0") long massId) {

        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        if(accountPO == null) {
            return new JsonResponse(ErrorCode.ACCOUNT_NOT_EXIST.getCode(),"", ImmutableMap.of());
        }

        if(isTiming != 0) {
            if(new Date(publishTime).before(new Date())) {
                return new JsonResponse(ErrorCode.MASS_TIMING_INVALID.getCode(),"", ImmutableMap.of());
            }
        }

        if(isPreview != 0) {
            if(StringUtils.isEmpty(accountPO.getPreviewOpenId())) {
                return new JsonResponse(ErrorCode.MASS_OPENID_NOT_SET.getCode(),"", ImmutableMap.of());
            }
        }

        String[] postIdArray = postIds.split(",");
        if(postIdArray == null || postIdArray.length ==0 ) {
            return new JsonResponse(ErrorCode.MASS_POSTID_NOT_SET.getCode(), "", ImmutableMap.of());
        }

        List<PostPO> postPOList = new ArrayList<>();
        for(String postId : postIdArray) {
            postPOList.add(postService.getPostByPageId(Integer.valueOf(postId)));
        }

        String content = null;
        MsgType type = MsgType.RESERVE;
        if(respType == 1) { // 文章列表
            type = MsgType.MP_NEWS;
            long postId;
            if(postPOList.size() > 1 && hasMulti != 1) {
                postId = postService.addMultiPosts(accountPO.getWeixinAppid(), postPOList);
            } else {
                postId = postPOList.get(0).getPageId();
            }
            PostPO post = postService.getPostByPageId(postId);
            String mediaId = post.getMediaId();
            // TODO
        } else if(respType == 3) { // 文字
            type = MsgType.TEXT;

        } else if(respType == 4) { // 图片
            type = MsgType.IMAGE;
        }

        if(isPreview != 0) {
            msgService.sendCustomMsg(accountPO.getWeixinAppid(), accountPO.getPreviewOpenId(), type, content);
        } else {
            // TODO
        }

        return new JsonResponse(ErrorCode.SUCCESS.getCode(), "", ImmutableMap.of());
    }



    /**
     * 客服群发是否有多图文 @url: custom-mass
     * @param massId
     * @return
     */
    @RequestMapping(value = "/mass/hasCustomMulti/{massId}", method = RequestMethod.GET)
    public JsonResponse hasCustomMulti (@PathVariable long massId) {
        MassPO massPO = massService.getMassById(massId);
        if(massPO.getResponseType() == MassPO.RespType.ARTICLE_LIST.getCode()) {
            List<MassRespPO> massRespPOList = JsonUtil.string2List(massPO.getResponseJson(), MassRespPO.class);
            if(massRespPOList != null && massRespPOList.size() > 0) {
                PostPO postPO = postService.getPostByPageId(massRespPOList.get(0).getPostId());
                if(postPO != null && postPO.getType() == PostPO.Type.Multi_One.getCode()) {
                    return new JsonResponse(true);
                }
            }
        }
        return new JsonResponse(false);
    }

    /**
     * 客服群发列表 @url: ajax-custom-mass-list
     * @param siteId
     * @param pageNO
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/mass/custom-list", method = RequestMethod.POST)
    public JsonResponse getCustomMassList(@RequestParam int siteId,
                                    @RequestParam(value = "pageNO", defaultValue = "1") int pageNO,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        if(accountPO != null) {
            
        }

        return new JsonResponse(null);
    }

    /**
     * 客服消息预览&群发 @url: ajax-custom-mass-preview ajax-custom-mass-new
     * @param siteId
     * @param respType
     * @param respJson
     * @param groupId
     * @param publishTime
     * @param isPreview
     * @param isTiming
     * @param hasMulti
     * @param massId
     * @return
     */
    @RequestMapping(value = "/mass/custom-msg/send", method = RequestMethod.POST)
    public JsonResponse sendCustomMassMsg(@RequestParam int siteId,
                                     @RequestParam(value = "response_type") int respType,
                                     @RequestParam(value = "response_json") String respJson,
                                     @RequestParam(value = "group_id") int groupId,
                                     @RequestParam(value = "publish_time") long publishTime,
                                     @RequestParam(value = "preview_or_not", defaultValue = "1") int isPreview,
                                     @RequestParam(value = "is_timing", defaultValue = "0") int isTiming,
                                     @RequestParam(value = "has_multi", defaultValue = "0") int hasMulti,
                                     @RequestParam(value = "mass_id", required = false, defaultValue = "0") long massId) {
        return new JsonResponse(null);
    }

}
