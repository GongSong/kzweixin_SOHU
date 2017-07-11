package com.kuaizhan.kzweixin.controller;

import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.dao.po.MassRespPO;
import com.kuaizhan.kzweixin.dao.po.PostPO;
import com.kuaizhan.kzweixin.dao.po.auto.AccountPO;
import com.kuaizhan.kzweixin.dao.po.auto.MassPO;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.MassService;
import com.kuaizhan.kzweixin.service.PostService;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    protected AccountService
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

        AccountPO accountPO = acc
        //List<MassPO> massPOList =
        return new JsonResponse(null);
    }

    /**
     * 获取群发对象分组 @url: ajax-mass-groups-get
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/mass/group/list", method = RequestMethod.POST)
    public JsonResponse getMassGroup(@RequestParam int siteId) {
        return new JsonResponse(null);
    }

    /**
     * 消息预览&群发 @url: ajax-mass-msg-new
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
    @RequestMapping(value = "/mass/msg/send", method = RequestMethod.POST)
    public JsonResponse sendMassMsg(@RequestParam int siteId,
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
