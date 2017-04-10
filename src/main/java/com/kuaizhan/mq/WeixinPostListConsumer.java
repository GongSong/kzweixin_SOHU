package com.kuaizhan.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuaizhan.config.MqConfig;
import com.kuaizhan.exception.BaseException;
import com.kuaizhan.exception.business.MaterialGetException;
import com.kuaizhan.pojo.DTO.PostDTO;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.PostService;
import com.kuaizhan.service.WeixinPostService;
import com.kuaizhan.utils.JsonUtil;
import com.kuaizhan.utils.LogUtil;
import com.kuaizhan.utils.MqUtil;

import java.util.*;

import javax.annotation.Resource;

/**
 * 图文消息导入
 *
 * Created by lorin on 17-3-29.
 */
public class WeixinPostListConsumer extends BaseMqConsumer {

    @Resource
    WeixinPostService weixinPostService;
    @Resource
    PostService postService;
    @Resource
    AccountService accountService;
    @Resource
    MqUtil mqUtil;

    @Override
    public void onMessage(Map msgMap) throws BaseException {
        long weixinAppid = (long) msgMap.get("weixinAppid");
        long userId = (long) msgMap.get("userId");

        // 获取不存在的微信图文
        List<PostDTO.PostItem> postItemList = postService.listNonExistsPostItemsFromWeixin(weixinAppid);

        // 分别导入
        for (PostDTO.PostItem postItem: postItemList) {
            Map<String, Object> message = new HashMap<>();
            message.put("userId", userId);
            try {
                String postItemJson = JsonUtil.bean2String(postItem);
                message.put("postItem", postItemJson);
                mqUtil.publish(MqConfig.IMPORT_KUAIZHAN_POST, message);
            } catch (JsonProcessingException e) {
                LogUtil.logMsg(e);
            }
        }
    }
}
