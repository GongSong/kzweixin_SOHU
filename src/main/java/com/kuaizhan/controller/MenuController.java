package com.kuaizhan.controller;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.constant.ResponseType;
import com.kuaizhan.param.common.WeixinAppidParam;
import com.kuaizhan.pojo.vo.JsonResponse;
import com.kuaizhan.service.MenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by zixiong on 2017/6/1.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION + "/menu", produces = AppConstant.PRODUCES)
public class MenuController extends BaseController {

    @Resource
    private MenuService menuService;

    @RequestMapping(value = "menu_keys", method = RequestMethod.POST)
    public JsonResponse genMenuKey(@Valid @RequestBody WeixinAppidParam param) {
        long menuKey = menuService.generateMenuItem(param.getWeixinAppid(), ResponseType.UNKNOWN, "");
        return new JsonResponse(ImmutableMap.of("menuKey", menuKey));
    }

    @RequestMapping(value = "default_menu", method = RequestMethod.GET)
    public JsonResponse getDefaultMenu(@RequestParam long weixinAppid) {
        return new JsonResponse(menuService.getMenu(weixinAppid));
    }
}
