/**
 * 公共模块
 */
package com.kuaizhan.controller;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.controller.vo.JsonResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用接口
 * Created by zixiong on 2017/4/16.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION + "/common", produces = "application/json")
public class CommonController extends BaseController{

    /**
     * ping, 用于安全检查
     */
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public JsonResponse ping(){
        return new JsonResponse(ImmutableMap.of());
    }
}
