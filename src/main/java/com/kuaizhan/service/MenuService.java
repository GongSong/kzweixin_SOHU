package com.kuaizhan.service;

import com.kuaizhan.pojo.dto.MenuDTO;
import com.kuaizhan.pojo.dto.MenuWrapper;

/**
 * Created by zixiong on 2017/5/25.
 */
public interface MenuService {
    /**
     * 更新menu
     * @param menuDTO menu对象，必须经过validate
     */
    void updateMenuJson(long weixinAppid, MenuDTO menuDTO);

    /**
     * 更新一个menu item的动作
     * @param menuKey menu id
     * @param responseType 回复类型，1 弹出文章列表，2 跳转到页面，3 弹出文字，4 弹出图片，5 跳转到外链
     * @param responseJson 回复的json
     * @param menuDTO 没更新一个menu item， 都会更新menu json
     */
    String updateMenuItem(long weixinAppid, long menuKey, int responseType, String responseJson, MenuDTO menuDTO);

    /**
     * 生成menu
     * @return menu key
     */
    long generateMenuItem(long weixinAppid, int responseType, String responseJson);


    /**
     * 获取默认menu
     * @return menuDTO 与 publish
     */
    MenuWrapper getMenu(long weixinAppid);
}
