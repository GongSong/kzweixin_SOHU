package com.kuaizhan.kzweixin.controller;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.controller.vo.CustomMassVO;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.controller.vo.MassVO;
import com.kuaizhan.kzweixin.dao.po.MassRespPO;
import com.kuaizhan.kzweixin.dao.po.PostPO;
import com.kuaizhan.kzweixin.dao.po.auto.AccountPO;
import com.kuaizhan.kzweixin.dao.po.auto.MassPO;
import com.kuaizhan.kzweixin.dao.po.auto.CustomMassPO;
import com.kuaizhan.kzweixin.entity.fan.TagDTO;
import com.kuaizhan.kzweixin.entity.msg.CustomMsg;
import com.kuaizhan.kzweixin.enums.MsgType;
import com.kuaizhan.kzweixin.service.*;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import com.kuaizhan.kzweixin.utils.PojoSwitcher;
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
    public JsonResponse getMassList(@RequestParam(value = "siteId") long siteId,
                                    @RequestParam(value = "pageNO", defaultValue = "1") int pageNO,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        List<MassPO> massPOList = null;
        List<MassVO> massVOList = new ArrayList<MassVO>();
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        if(accountPO != null && accountPO.getWeixinAppid() != 0) {
            massPOList = massService.getMassByWxAppId(accountPO.getWeixinAppid());
        }
        for (MassPO massPO: massPOList) {
            MassVO massVo = PojoSwitcher.MassPOToVO(massPO);
            massVOList.add(massVo);
        }
        return new JsonResponse(massVOList);
    }

    /**
     * 获取群发对象tag分组 @url: ajax-mass-groups-get
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/mass/tags/list", method = RequestMethod.POST)
    public JsonResponse getMassGroup(@RequestParam(value = "siteId")  long siteId) {

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
     * @param respType 旧接口类型, 1图文 3文字 4图片, 新接口类型 9图文 1文字 2图片
     * @param respJson 如果是图文应为PostId数组, 如果其他类型请参考 MassMsg和CustomMsg的内部类, 示例: 图文{[1,2,3]} 文字{"content":"sss.."} 图片{"media_id":"xxx"}
     * @param tagId 发送用户的标签id, 0为发送给全部标签
     * @param publishTime 发布时间，定时群发时才有意义
     * @param isPreview 1表示预览 0表示群发
     * @param isTiming 1表示定时发布，0不定时
     * @param isMulti 是否多图文
     * @param massId 0
     * @return
     */
    @RequestMapping(value = "/mass/msg/send", method = RequestMethod.POST)
    public JsonResponse sendMassMsg(@RequestParam(value = "siteId") long siteId,
                                     @RequestParam(value = "response_type") short respType,
                                     @RequestParam(value = "response_json", required = false, defaultValue = "") String respJson,
                                     @RequestParam(value = "tag_id") int tagId,
                                     @RequestParam(value = "publish_time") long publishTime,
                                     @RequestParam(value = "preview_or_not", defaultValue = "1") int isPreview,
                                     @RequestParam(value = "is_timing", defaultValue = "0") int isTiming,
                                     @RequestParam(value = "has_multi", defaultValue = "0") int isMulti,
                                     @RequestParam(value = "mass_id", required = false, defaultValue = "0") long massId) {

        // TODO: 定时发送, 创建新MassID
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
                return new JsonResponse(ErrorCode.MASS_OPENID_INVALID.getCode(),ErrorCode.MASS_OPENID_INVALID.getMessage(), ImmutableMap.of());
            }
        }

        MsgType type = MsgType.fromValue(respType);
        if(massService.checkSupportType(type) == false) {
            return new JsonResponse(ErrorCode.MASS_TYPE_INVALID.getCode(), ErrorCode.MASS_NOT_EXIST.getMessage(), ImmutableMap.of());
        }

        if(isPreview != 0) {
            String msg = massService.wrapPreviewMsg(accountPO.getWeixinAppid(), type, respJson, isMulti);
            msgService.sendCustomMsg(accountPO.getWeixinAppid(), accountPO.getPreviewOpenId(), type, msg);
        } else {
            Object msg = massService.wrapMassMsg(accountPO.getWeixinAppid(), type, respJson, isMulti);
            massService.sendMassMsg(accountPO.getWeixinAppid(), tagId, type, msg);
        }

        return new JsonResponse(ErrorCode.SUCCESS.getCode(), "success", ImmutableMap.of());
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
    public JsonResponse getCustomMassList(@RequestParam(value = "siteId") long siteId,
                                    @RequestParam(value = "pageNO", defaultValue = "1") int pageNO,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        List<CustomMassPO> customMassPOList = null;
        List<CustomMassVO> customMassVOList = new ArrayList<CustomMassVO>();
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        if(accountPO != null&& accountPO.getWeixinAppid() != 0) {
            customMassPOList = massService.getCustomMassByWxAppId(accountPO.getWeixinAppid());
        } else

        for (CustomMassPO customMassPO: customMassPOList) {
            CustomMassVO CustomMassVo = PojoSwitcher.CustomMassPOToVO(customMassPO);
            customMassVOList.add(CustomMassVo);
        }



        return new JsonResponse(customMassVOList);
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
    public JsonResponse sendCustomMassMsg(@RequestParam(value = "siteId")  long siteId,
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
