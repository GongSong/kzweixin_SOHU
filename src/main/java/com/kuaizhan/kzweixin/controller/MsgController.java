package com.kuaizhan.kzweixin.controller;


import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.controller.vo.MsgVO;
import com.kuaizhan.kzweixin.dao.po.auto.AccountPO;
import com.kuaizhan.kzweixin.entity.common.PageV2;
import com.kuaizhan.kzweixin.entity.msg.CustomMsg;
import com.kuaizhan.kzweixin.enums.MsgType;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.controller.vo.MsgListVO;
import com.kuaizhan.kzweixin.controller.param.WeixinAppidParam;
import com.kuaizhan.kzweixin.controller.param.SendCustomMsgParam;
import com.kuaizhan.kzweixin.controller.param.UpdateQuickRepliesParam;
import com.kuaizhan.kzweixin.dao.po.auto.FanPO;
import com.kuaizhan.kzweixin.dao.po.MsgPO;
import com.kuaizhan.kzweixin.entity.common.Page;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.FanService;
import com.kuaizhan.kzweixin.service.MsgService;

import com.kuaizhan.kzweixin.utils.JsonUtil;
import com.kuaizhan.kzweixin.utils.PojoSwitcher;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.*;

/**
 * 消息模块接口
 * Created by Mr.Jadyn on 2016/12/29.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION, produces = "application/json")
public class MsgController extends BaseController {

    @Resource
    private MsgService msgService;
    @Resource
    private FanService fanService;
    @Resource
    private AccountService accountService;

    /**
     * 获取消息列表
     */
    @RequestMapping(value = "/msg/msgs", method = RequestMethod.GET)
    public JsonResponse getMsgs(@RequestParam long weixinAppid,
                                @RequestParam(defaultValue = "0") int offset,
                                @RequestParam(defaultValue = "20") int limit,
                                @RequestParam(required = false) String queryStr,
                                @RequestParam(required = false, defaultValue = "0") boolean filterKeywords) {

        PageV2<MsgPO> msgPOPage = msgService.listMsgsByPage(weixinAppid, queryStr, filterKeywords, offset, limit);

        List<MsgPO> msgPOS = msgPOPage.getDataSet();

        List<MsgVO> msgVOS = new ArrayList<>();
        for(MsgPO msgPO: msgPOS) {
            msgVOS.add(PojoSwitcher.msgPOToVO(msgPO));
        }
        return new JsonResponse(ImmutableMap.of("total", msgPOPage.getTotal(), "msgs", msgVOS));
    }

    /**
     * 获取和用户的聊天列表
     */
    @RequestMapping(value = "/msg/chat_list", method = RequestMethod.GET)
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
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        FanPO fanPO = fanService.getFanByOpenId(accountPO.getAppId(), openId);
        msgListVO.setLastInteractTime(fanPO.getLastInteractTime());

        return new JsonResponse(msgListVO);
    }

    /**
     * 获取未读消息数
     */
    @RequestMapping(value = "/msg/unread_msg_count", method = RequestMethod.GET)
    public JsonResponse getUnreadCount(@RequestParam long weixinAppid) {
        long count = msgService.getUnreadMsgCount(weixinAppid);
        return new JsonResponse(ImmutableMap.of("count", count));
    }

    /**
     * 标记消息已读(更新last_read_time)
     */
    @RequestMapping(value = "/msg/msg_reads", method = RequestMethod.POST)
    public JsonResponse markMsgsRead(@Valid @RequestBody WeixinAppidParam param) {
        msgService.markMsgRead(param.getWeixinAppid());
        return new JsonResponse(ImmutableMap.of());

    }

    /**
     * 获取快速回复配置的接口
     */
    @RequestMapping(value = "/msg/quick_replies", method = RequestMethod.GET)
    public JsonResponse getQuickReplies(@RequestParam long weixinAppid) {
        List<String> quickReplies = msgService.getQuickReplies(weixinAppid);
        return new JsonResponse(ImmutableMap.of("quickReplies", quickReplies));
    }

    /**
     * 修改快速回复配置
     */
    @RequestMapping(value = "/msg/quick_replies", method = RequestMethod.PUT)
    public JsonResponse updateQuickReplies(@Valid @RequestBody UpdateQuickRepliesParam param) {
        msgService.updateQuickReplies(param.getWeixinAppid(), param.getQuickReplies());
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 发送客服消息
     */
    @RequestMapping(value = "/msg/msgs", method = RequestMethod.POST)
    public JsonResponse insertCustomMsg(@Valid @RequestBody SendCustomMsgParam param) {
        MsgType msgType = MsgType.fromValue(param.getMsgType());
        String contentJsonStr = JsonUtil.bean2String(param.getContent());

        CustomMsg customMsg = new CustomMsg();
        customMsg.setMsgType(msgType);

        if (msgType == MsgType.TEXT) {
            customMsg.setText(JsonUtil.string2Bean(contentJsonStr, CustomMsg.Text.class));
        } else if (msgType == MsgType.IMAGE) {
            customMsg.setImage(JsonUtil.string2Bean(contentJsonStr, CustomMsg.Image.class));
        } else if (msgType == MsgType.MP_NEWS) {
            customMsg.setMpNews(JsonUtil.string2Bean(contentJsonStr, CustomMsg.MpNews.class));
        } else if (msgType == MsgType.LINK_GROUP) {
            customMsg.setNews(JsonUtil.string2Bean(contentJsonStr, CustomMsg.News.class));
        }

        msgService.sendCustomMsg(param.getWeixinAppid(), param.getOpenId(), customMsg);
        return new JsonResponse(ImmutableMap.of());
    }

    /**
     * 获取快站推送token
     */
    @RequestMapping(value = "/msg/push_token", method = RequestMethod.GET)
    public JsonResponse getPushToken(@RequestParam long weixinAppid, @RequestParam String openId) {
        String token = msgService.getPushToken(weixinAppid, openId);
        return new JsonResponse(ImmutableMap.of("token", token));
    }

    /**
     * 关闭推送token
     */
    @RequestMapping(value = "/msg/push_token", method = RequestMethod.DELETE)
    public JsonResponse deletePushToken(@RequestParam long weixinAppid, @RequestParam String openId) {
        msgService.deletePushToken(weixinAppid, openId);
        return new JsonResponse(ImmutableMap.of());
    }
}
