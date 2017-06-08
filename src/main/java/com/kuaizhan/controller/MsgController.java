package com.kuaizhan.controller;


import com.google.common.collect.ImmutableMap;
import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.constant.MsgType;
import com.kuaizhan.param.common.WeixinAppidParam;
import com.kuaizhan.param.msg.SendCustomMsgParam;
import com.kuaizhan.param.msg.UpdateQuickRepliesParam;

import com.kuaizhan.pojo.po.FanPO;
import com.kuaizhan.pojo.po.MsgPO;
import com.kuaizhan.pojo.dto.Page;
import com.kuaizhan.pojo.vo.*;
import com.kuaizhan.service.FanService;
import com.kuaizhan.service.MsgService;

import com.kuaizhan.utils.JsonUtil;
import com.kuaizhan.utils.PojoSwitcher;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.*;

/**
 * 消息模块接口
 * Created by Mr.Jadyn on 2016/12/29.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION + "/msg", produces = "application/json")
public class MsgController extends BaseController {

    @Resource
    private MsgService msgService;
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

    /**
     * 获取快速回复配置的接口
     */
    @RequestMapping(value = "/quick_replies", method = RequestMethod.GET)
    public JsonResponse getQuickReplies(@RequestParam long weixinAppid) {
        List<String> quickReplies = msgService.getQuickReplies(weixinAppid);
        return new JsonResponse(ImmutableMap.of("quickReplies", quickReplies));
    }

    /**
     * 修改快速回复配置
     */
    @RequestMapping(value = "/quick_replies", method = RequestMethod.PUT)
    public JsonResponse updateQuickReplies(@Valid @RequestBody UpdateQuickRepliesParam param) {
        msgService.updateQuickReplies(param.getWeixinAppid(), param.getQuickReplies());
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 发送客服消息
     */
    @RequestMapping(value = "/msgs", method = RequestMethod.POST)
    public JsonResponse insertCustomMsg(@Valid @RequestBody SendCustomMsgParam param) {
        MsgType msgType = MsgType.fromValue(param.getMsgType());
        msgService.sendCustomMsg(param.getWeixinAppid(), param.getOpenId(), msgType, JsonUtil.bean2String(param.getContent()));
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 获取快站推送token
     */
    @RequestMapping(value = "push_token", method = RequestMethod.GET)
    public JsonResponse getPushToken(@RequestParam long weixinAppid, @RequestParam String openId) {
        String token = msgService.getPushToken(weixinAppid, openId);
        return new JsonResponse(ImmutableMap.of("token", token));
    }

    /**
     * 关闭推送token
     */
    @RequestMapping(value = "push_token", method = RequestMethod.DELETE)
    public JsonResponse deletePushToken(@RequestParam long weixinAppid, @RequestParam String openId) {
        msgService.deletePushToken(weixinAppid, openId);
        return new JsonResponse(ImmutableMap.of());
    }
}
