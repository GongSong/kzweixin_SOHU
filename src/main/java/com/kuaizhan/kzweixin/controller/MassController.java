package com.kuaizhan.kzweixin.controller;

import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.dao.po.MassRespPO;
import com.kuaizhan.kzweixin.dao.po.PostPO;
import com.kuaizhan.kzweixin.dao.po.auto.MassPO;
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

    /**
     * 是否有多图文
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
}
