package com.kuaizhan.controller;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.exception.deprecated.business.ParamException;
import com.kuaizhan.exception.deprecated.business.SendCustomMsgException;
import com.kuaizhan.exception.common.DaoException;
import com.kuaizhan.exception.deprecated.system.JsonParseException;
import com.kuaizhan.exception.common.RedisException;
import com.kuaizhan.param.common.WeixinAppidParam;
import com.kuaizhan.param.msg.UpdateQuickRepliesParam;
import com.kuaizhan.pojo.po.AccountPO;

import com.kuaizhan.pojo.po.FanPO;
import com.kuaizhan.pojo.po.MsgPO;
import com.kuaizhan.pojo.dto.Page;
import com.kuaizhan.pojo.vo.*;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.FanService;
import com.kuaizhan.service.MsgService;

import com.kuaizhan.utils.PojoSwitcher;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.*;

/**
 * 消息管理controller
 * Created by Mr.Jadyn on 2016/12/29.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION + "/msg", produces = "application/json")
public class MsgController extends BaseController {

    @Resource
    private MsgService msgService;
    @Resource
    private AccountService accountService;
    @Resource
    private FanService fanService;

    /**
     * 获取消息列表
     */
    @RequestMapping(value = "/msgs", method = RequestMethod.GET)
    public JsonResponse getMsgs(@RequestParam long weixinAppid, @RequestParam int page,
                                 @RequestParam(required = false) String queryStr,
                                 @RequestParam(required = false, defaultValue = "0") boolean filterKeywords) {
        Page<MsgPO> msgPOPage = msgService.listMsgsByPagination(weixinAppid, queryStr, filterKeywords, page);
        List<MsgPO> msgPOS = msgPOPage.getResult();

        MsgListVO msgListVO = new MsgListVO();
        msgListVO.setTotalNum(msgPOPage.getTotalCount());
        msgListVO.setTotalPage(msgPOPage.getTotalPages());
        msgListVO.setCurrentPage(msgPOPage.getPageNo());

        for(MsgPO msgPO: msgPOS) {
            msgListVO.getMsgs().add(PojoSwitcher.msgPOToVO(msgPO));
        }
        return new JsonResponse(msgListVO);
    }

    /**
     * 获取和用户的聊天列表
     */
    @RequestMapping(value = "/chat_list", method = RequestMethod.GET)
    public JsonResponse getChatList(@RequestParam long weixinAppid, @RequestParam int page, @RequestParam String openId) {
        Page<MsgPO> msgPOPage = msgService.listMsgsByOpenId(weixinAppid, openId, page);
        List<MsgPO> msgPOS = msgPOPage.getResult();

        MsgListVO msgListVO = new MsgListVO();
        msgListVO.setTotalNum(msgPOPage.getTotalCount());
        msgListVO.setTotalPage(msgPOPage.getTotalPages());
        msgListVO.setCurrentPage(msgPOPage.getPageNo());

        for(MsgPO msgPO: msgPOS) {
            msgListVO.getMsgs().add(PojoSwitcher.msgPOToVO(msgPO));
        }

        // 获取粉丝的最后交互时间
        FanPO fanPO = fanService.getFanByOpenId(weixinAppid, openId);
        msgListVO.setLastInteractTime(fanPO.getLastInteractTime());

        return new JsonResponse(msgListVO);
    }

    /**
     * 获取未读消息数
     */
    @RequestMapping(value = "/unread_msg_count", method = RequestMethod.GET)
    public JsonResponse getUnreadCount(@RequestParam long weixinAppid) {
        long count = msgService.getUnreadMsgCount(weixinAppid);
        return new JsonResponse(ImmutableMap.of("count", count));
    }

    /**
     * 标记消息已读(更新last_read_time)
     */
    @RequestMapping(value = "/msg_reads", method = RequestMethod.POST)
    public JsonResponse markMsgsRead(@Valid @RequestBody WeixinAppidParam param) {
        msgService.markMsgRead(param.getWeixinAppid());
        return new JsonResponse(ImmutableMap.of());

    }

    @RequestMapping(value = "/quick_replies", method = RequestMethod.GET)
    public JsonResponse getQuickReplies(@RequestParam long weixinAppid) {
        List<String> quickReplies = msgService.getQuickReplies(weixinAppid);
        return new JsonResponse(ImmutableMap.of("quickReplies", quickReplies));
    }

    @RequestMapping(value = "/quick_replies", method = RequestMethod.PUT)
    public JsonResponse updateQuickReplies(@Valid @RequestBody UpdateQuickRepliesParam param) {
        msgService.updateQuickReplies(param.getWeixinAppid(), param.getQuickReplies());
        return new JsonResponse(ImmutableMap.of());
    }



    /**
     * 给用户发送客服消息
     *
     * @param siteId 站点id
     * @param openId 用户openId
     * @return
     */
    @RequestMapping(value = "/msgs/{openId}", method = RequestMethod.POST)
    public JsonResponse insertCustomMsg(@RequestParam long siteId, @PathVariable String openId, @RequestBody String postData) throws ParamException, DaoException, RedisException, SendCustomMsgException, JsonParseException {
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);

        int msgType;
        JSONObject afterValidation = new JSONObject();
        JSONObject jsonObject = new JSONObject(postData);
        if (jsonObject.has("content")) {
            msgType = 1;
            afterValidation.put("content", jsonObject.getString("content"));
        } else if (jsonObject.has("media_id")) {
            msgType = 2;
            afterValidation.put("media_id", jsonObject.getString("media_id"));
        } else if (jsonObject.has("articles")) {
            msgType = 10;
            afterValidation.put("articles", jsonObject.getJSONArray("articles"));
        } else {
            throw new ParamException();
        }
        msgService.insertCustomMsg(accountPO, openId, msgType, afterValidation);
        return new JsonResponse(null);
    }
}
