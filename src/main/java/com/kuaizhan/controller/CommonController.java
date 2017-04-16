/**
 * 公共模块
 */
package com.kuaizhan.controller;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.pojo.VO.JsonResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zixiong on 2017/4/16.
 */
@RestController
@RequestMapping(value = ApplicationConfig.VERSION + "/common", produces = "application/json")
public class CommonController extends BaseController{

    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    public JsonResponse ping(){
        return new JsonResponse(null);
    }
}
