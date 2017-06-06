package com.kuaizhan.controller;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.constant.ResponseType;
import com.kuaizhan.param.common.WeixinAppidParam;
import com.kuaizhan.pojo.dto.MenuWrapper;
import com.kuaizhan.pojo.po.auto.WeixinConditionalMenu;
import com.kuaizhan.pojo.vo.ConditionalMenuVO;
import com.kuaizhan.pojo.vo.JsonResponse;
import com.kuaizhan.service.MenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单模块接口
 * Created by zixiong on 2017/6/1.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION + "/menu", produces = AppConstant.PRODUCES)
public class MenuController extends BaseController {

    @Resource
    private MenuService menuService;

    /**
     * 新增menu key
     */
    @RequestMapping(value = "menu_keys", method = RequestMethod.POST)
    public JsonResponse genMenuKey(@Valid @RequestBody WeixinAppidParam param) {
        long menuKey = menuService.generateMenuItem(param.getWeixinAppid(), ResponseType.UNKNOWN, "");
        return new JsonResponse(ImmutableMap.of("menuKey", menuKey));
    }

    /**
     * 获取默认菜单
     */
    @RequestMapping(value = "default_menu", method = RequestMethod.GET)
    public JsonResponse getDefaultMenu(@RequestParam long weixinAppid) {
        MenuWrapper menuWrapper = menuService.getMenu(weixinAppid);
        if (menuWrapper.getPublish() == null) {
            menuWrapper.setPublish(false);
        }
        return new JsonResponse(menuWrapper);
    }

    /**
     * 获取个性化菜单列表
     */
    @RequestMapping(value = "conditional_menus", method = RequestMethod.GET)
    public JsonResponse getConditionalMenus(@RequestParam long weixinAppid) {
        List<WeixinConditionalMenu> menus = menuService.getConditionalMenus(weixinAppid);
        List<ConditionalMenuVO> menuVOS = new ArrayList<>();

        for (WeixinConditionalMenu menu: menus) {
            ConditionalMenuVO menuVO = new ConditionalMenuVO();
            menuVO.setId(menu.getId());
            menuVO.setMenuId(menu.getMenuId());
            menuVO.setMenuName(menu.getMenuName());
            menuVOS.add(menuVO);
        }

        return new JsonResponse(menuVOS);
    }
}
