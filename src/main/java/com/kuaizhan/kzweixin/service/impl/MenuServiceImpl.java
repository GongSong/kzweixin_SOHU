package com.kuaizhan.kzweixin.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.constant.ResponseType;
import com.kuaizhan.kzweixin.dao.mapper.AccountDao;
import com.kuaizhan.kzweixin.dao.mapper.auto.ConditionMenuMapper;
import com.kuaizhan.kzweixin.dao.mapper.auto.MenuItemMapper;
import com.kuaizhan.kzweixin.exception.BusinessException;
import com.kuaizhan.kzweixin.exception.menu.MenuCheckException;
import com.kuaizhan.kzweixin.entity.menu.LinkList;
import com.kuaizhan.kzweixin.entity.menu.MenuDTO;
import com.kuaizhan.kzweixin.entity.menu.MenuWrapper;
import com.kuaizhan.kzweixin.dao.po.AccountPO;
import com.kuaizhan.kzweixin.dao.po.PostPO;
import com.kuaizhan.kzweixin.dao.po.auto.ConditionMenuPO;
import com.kuaizhan.kzweixin.dao.po.auto.ConditionMenuPOExample;
import com.kuaizhan.kzweixin.dao.po.auto.MenuItemPO;
import com.kuaizhan.kzweixin.dao.po.auto.MenuItemPOExample;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.MenuService;
import com.kuaizhan.kzweixin.service.PostService;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import com.kuaizhan.kzweixin.utils.UrlUtil;
import com.vdurmont.emoji.EmojiParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by zixiong on 2017/5/25.
 */
@Service("menuService")
public class MenuServiceImpl implements MenuService {

    @Resource
    private AccountService accountService;
    @Resource
    private AccountDao accountDao;
    @Resource
    private MenuItemMapper menuItemMapper;
    @Resource
    private ConditionMenuMapper conditionMenuMapper;
    @Resource
    private PostService postService;

    private static final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Override
    public void updateMenuJson(long weixinAppid, MenuDTO menuDTO) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String menuJson = accountPO.getMenuJson();

        /* 对比差异，删除不再存在的menuKey  真的需要这一步？ */
        // 现在的所有menu key
        List<Long> keys = new ArrayList<>();
        for (MenuDTO.Button button: menuDTO.getButtons()) {
            keys.add(button.getKey());
            for (MenuDTO.Button subButton: button.getSubButton()) {
                keys.add(subButton.getKey());
            }
        }

        // 删除废弃的menu item
        MenuWrapper menuWrapper = JsonUtil.string2Bean(menuJson, MenuWrapper.class);
        MenuDTO oldMenuDTO = menuWrapper.getMenu();
        if (oldMenuDTO != null) {
            for (MenuDTO.Button button: oldMenuDTO.getButtons()) {
                Long key = button.getKey();
                if (!keys.contains(key)) {
                    deleteMenuItem(key);
                }

                for (MenuDTO.Button subButton: button.getSubButton()) {
                    key = subButton.getKey();
                    if (!keys.contains(key)) {
                        deleteMenuItem(key);
                    }
                }
            }
        }

        // 更新menu json
        MenuWrapper menu = new MenuWrapper();
        menu.setMenu(menuDTO);

        AccountPO account = new AccountPO();
        account.setWeixinAppId(weixinAppid);
        account.setMenuJson(JsonUtil.bean2String(menu));

        accountDao.updateAccountByWeixinAppId(accountPO);
    }

    @Override
    public String updateMenuItem(long weixinAppid, long menuKey, int responseType, String responseJson, MenuDTO menuDTO){
        String linListResponseJson = responseJson;
        // 清理数据
        responseJson = cleanResponseJson(weixinAppid, responseType, responseJson);

        // 修改menuItem
        MenuItemPO record = new MenuItemPO();
        record.setMenuKey(menuKey);
        record.setResponseType(responseType);
        record.setResponseJson(responseJson);
        record.setUpdateTime(DateUtil.curSeconds());
        int rows = menuItemMapper.updateByPrimaryKeySelective(record);
        if (rows == 0) {
            logger.error("菜单项不存在, weixinAppid:{} menuKey:{}", weixinAppid, menuKey);
            throw new BusinessException(ErrorCode.OPERATION_FAILED, "菜单项不存在，请重试");
        }

        // 修改menuItem, menuJson也会有改动
        updateMenuJson(weixinAppid, menuDTO);

        // 链接组返回原始数据
        if (responseType == ResponseType.LINK_LIST) {
            return linListResponseJson;
        }
        return responseJson;
    }


    @Override
    public long generateMenuItem(long weixinAppid, int responseType, String responseJson) {

        final long MIN = 1000000000L;
        final long MAX = 9999999999L;
        int count = 1;

        long menuKey = ThreadLocalRandom.current().nextLong(MIN, MAX + 1);
        // 是否已存在
        while (menuItemMapper.selectByPrimaryKey(menuKey) != null) {
            menuKey = ThreadLocalRandom.current().nextLong(MIN, MAX + 1);
            count++;
        }
        // 随机三次及以上才随机到，报警
        if (count >= 3) {
            logger.warn("[genPageId] gen times reach {}", count);
        }

        MenuItemPO record = new MenuItemPO();
        record.setWeixinAppid(weixinAppid);
        record.setMenuKey(menuKey);
        record.setResponseType(responseType);
        record.setResponseJson(responseJson);
        record.setStatus(1);
        record.setCreateTime(DateUtil.curSeconds());
        record.setUpdateTime(DateUtil.curSeconds());
        menuItemMapper.insert(record);

        return menuKey;
    }

    @Override
    public MenuWrapper getMenu(long weixinAppid) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String menuJson = accountPO.getMenuJson();
        if ("".equals(menuJson) || menuJson == null) {
            menuJson = "{}";
        }
        return JsonUtil.string2Bean(menuJson, MenuWrapper.class);
    }

    @Override
    public List<ConditionMenuPO> getConditionalMenus(long weixinAppid) {
        ConditionMenuPOExample example = new ConditionMenuPOExample();
        example.createCriteria()
                .andWeixinAppidEqualTo(weixinAppid)
                .andStatusEqualTo(1);
        example.setOrderByClause("update_time desc");
        return conditionMenuMapper.selectByExample(example);
    }

    /**
     * 检测菜单是否设置动作
     * @throws MenuCheckException: 菜单有未设置动作的
     */
    private void checkMenu(MenuDTO menuDTO) throws MenuCheckException {

        for (MenuDTO.Button button: menuDTO.getButtons()) {
            // 没有子菜单
            if (button.getSubButton().size() == 0) {
                checkButton(button);
            } else {
                for (MenuDTO.Button subButton: button.getSubButton()) {
                    checkButton(subButton);
                }
            }
        }
    }

    /**
     * 检查每个应该设置动作的button
     * @throws MenuCheckException: 菜单有未设置动作的
     */
    private void checkButton(MenuDTO.Button button) throws MenuCheckException {
        String type = button.getType();
        // type和key是否为空
        if (type == null || button.getKey() == null) {
            throw new MenuCheckException("菜单【" + button.getName() + "】没有设置动作");
        }
        // key是否存在, 是否有必要?
        MenuItemPOExample example = new MenuItemPOExample();
        example.createCriteria()
                .andMenuKeyEqualTo(button.getKey())
                .andStatusEqualTo(1);
        if (menuItemMapper.selectByExample(example).size() == 0) {
            throw new MenuCheckException("菜单【" + button.getName() + "】没有设置动作");
        }
        // view类型必须设置url
        if ("view".equals(type)) {
            // 真的走得到这一步吗？ 垃圾数据才可以吧, 或许这里需要的是bean validate
            if (button.getUrl() == null || "".equals(button.getUrl())) {
                throw new MenuCheckException("菜单【" + button.getName() + "】没有设置动作");
            }
        }
        //TODO: 此处省略了认证订阅号新认证的逻辑，找时间问戴尚静
    }

    /**
     * 修正response json数据
     * @throws IllegalArgumentException: 未知的responseType
     */
    private String cleanResponseJson(long weixinAppid, int responseType, String responseJson) throws IllegalArgumentException {
        // 图文列表
        if (responseType == ResponseType.POST_LIST) {

            ObjectNode resultJson = new ObjectMapper().createObjectNode();
            ArrayNode postList = resultJson.putArray("post_list");

            int hasMulti = 0;
            JSONArray postArray = new JSONArray(responseJson);
            for (int i = 0; i < postArray.length(); i++) {
                JSONObject jsonObject = postArray.getJSONObject(i);
                long postId = jsonObject.getLong("post_id");

                PostPO postPO = postService.getPostByPageId(postId);
                if (postPO.getType() == 3) {
                    hasMulti = 1;
                }
                // TODO: 这个地方要加上异常处理
                String wxUrl = postService.getPostWxUrl(weixinAppid, postId);
                ObjectNode node = new ObjectMapper().createObjectNode()
                        .put("post_id", postPO.getPageId())
                        .put("post_title", postPO.getTitle())
                        .put("post_description", postPO.getDigest() == null? "": postPO.getDigest())
                        .put("post_pic_url", postPO.getThumbUrl())
                        .put("post_url", wxUrl)
                        .put("post_type", 1);
                postList.add(node);
            }
            resultJson.put("has_multi", hasMulti);
            return resultJson.toString();
        }
        // 链接
        else if (responseType == ResponseType.LINK) {
            // 这里要有校验, 还是把校验放外面
            return responseJson;
        }
        // 文本
        else if (responseType == ResponseType.TEXT) {
            JSONObject jsonObject = new JSONObject(responseJson);
            String content = jsonObject.getString("content");
            ObjectNode resultJson = new ObjectMapper().createObjectNode()
                    .put("content", EmojiParser.removeAllEmojis(content));
            return resultJson.toString();
        }
        // 链接组
        else if (responseType == ResponseType.LINK_LIST) {
            List<LinkList> linkLists = JsonUtil.string2List(responseJson, LinkList.class);

            ObjectNode resultJson = new ObjectMapper().createObjectNode();
            ArrayNode urlList = resultJson.putArray("url_list");
            for(LinkList linkList: linkLists) {
                ObjectNode node = new ObjectMapper().createObjectNode()
                        .put("post_title", EmojiParser.removeAllEmojis(linkList.getTitle()))
                        .put("post_description", linkList.getSummary())
                        .put("post_pic_url", UrlUtil.fixProtocol(linkList.getCover()))
                        .put("post_url", linkList.getLink())
                        .put("link_res_id", linkList.getLinkResId())
                        .put("link_res_type", linkList.getLinkResType())
                        .put("link_res_name", linkList.getLinkResName());
                urlList.add(node);
            }
            return resultJson.toString();
        }
        throw new IllegalArgumentException("[Menu:formatResponseJson] unknown responseType:" + responseType);
    }

    /**
     * 删除一个menu
     */
    private void deleteMenuItem(long menuKey) {
        MenuItemPO menuItem = new MenuItemPO();
        menuItem.setMenuKey(menuKey);
        menuItem.setStatus(2);
        menuItem.setUpdateTime(DateUtil.curSeconds());
        menuItemMapper.updateByPrimaryKeySelective(menuItem);
    }
}
