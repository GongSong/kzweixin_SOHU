package com.kuaizhan.controller;


import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.exception.deprecated.business.ParamException;
import com.kuaizhan.exception.deprecated.business.SendCustomMsgException;
import com.kuaizhan.exception.common.DaoException;
import com.kuaizhan.exception.deprecated.system.JsonParseException;
import com.kuaizhan.exception.common.RedisException;
import com.kuaizhan.pojo.po.AccountPO;

import com.kuaizhan.pojo.po.FanPO;
import com.kuaizhan.pojo.po.MsgPO;
import com.kuaizhan.pojo.dto.Page;
import com.kuaizhan.pojo.vo.*;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.FanService;
import com.kuaizhan.service.MsgService;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.*;

/**
 * 消息管理controller
 * Created by Mr.Jadyn on 2016/12/29.
 */
@RestController
@RequestMapping(value = AppConstant.VERSION, produces = "application/json")
public class MsgController extends BaseController {

    @Resource
    MsgService msgService;
    @Resource
    AccountService accountService;
    @Resource
    FanService fanService;

    /**
     * 获取消息列表
     *
     * @param siteId 站点id
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/msgs", method = RequestMethod.GET)
    public JsonResponse listMsgs(@RequestParam long siteId, @RequestParam int page, @RequestParam int isHide, @RequestParam(required = false) String keyword) throws RedisException, DaoException, JsonParseException {
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        Page<MsgPO> pagingResult = msgService.listMsgsByPagination(siteId, accountPO.getAppId(), page, keyword, isHide);
        return new JsonResponse(handleData(pagingResult));
    }

    /**
     * 获取新发消息数量
     *
     * @param siteId 站点id
     * @return
     */
    @RequestMapping(value = "/msgs/new/count", method = RequestMethod.GET)
    public JsonResponse getNewMsgsNum(@RequestParam long siteId) throws DaoException, RedisException, JsonParseException {
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        long num = msgService.countMsg(accountPO.getAppId(), 1, 0, null, 0);
        Map<String, Object> data = new HashMap<>();
        data.put("num", num);
        return new JsonResponse(data);
    }

    /**
     * 点击查看新消息
     *
     * @param siteId 站点id
     * @return
     */
    @RequestMapping(value = "/msgs/new", method = RequestMethod.GET)
    public JsonResponse listNewMsgs(@RequestParam long siteId) throws IOException, DaoException, RedisException, JsonParseException {
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        List<MsgPO> msgs = msgService.listNewMsgs(accountPO.getAppId());
        if (msgs.size() > 0) {
            msgService.updateMsgsStatus(siteId, accountPO.getAppId(), msgs);
        }
        Page<MsgPO> pagingResult = msgService.listMsgsByPagination(siteId, accountPO.getAppId(), 1, null, 0);
        return new JsonResponse(handleData(pagingResult));
    }

    /**
     * 获取单个用户的消息
     *
     * @param siteId 站点id
     * @param openId 用户的openId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/msgs/{openId}", method = RequestMethod.GET)
    public JsonResponse listMsgsByOpenId(@RequestParam long siteId, @RequestParam int page, @PathVariable String openId) throws DaoException, RedisException, JsonParseException {
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        Page<MsgPO> msgs = msgService.listMsgsByOpenId(siteId, accountPO.getAppId(), openId, page);
        if (msgs.getResult() != null) {
            UserMsgListVO userMsgListVO = new UserMsgListVO();

            for (MsgPO msgPO : msgs.getResult()) {
                UserMsgVO userMsgVO = new UserMsgVO();
                userMsgVO.setId(msgPO.getMsgId());
                userMsgVO.setSendType(msgPO.getSendType());
                userMsgVO.setContent(msgPO.getContent());
                userMsgVO.setTime(msgPO.getCreateTime());
                userMsgListVO.getMsgs().add(userMsgVO);
            }
            FanPO fanPO = fanService.getFanByOpenId(accountPO.getAppId(), openId);
            if ((System.currentTimeMillis() / 1000 - fanPO.getLastInteractTime()) > 48 * 3600)
                userMsgListVO.setIsExpire(1);
            else
                userMsgListVO.setIsExpire(0);
            return new JsonResponse(userMsgListVO);
        }
        return new JsonResponse(null);
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


    private MsgListVO handleData(Page<MsgPO> pagingResult) {
        if (pagingResult.getResult() != null) {
            MsgListVO msgListVO = new MsgListVO();
            msgListVO.setTotalNum(pagingResult.getTotalCount());
            msgListVO.setTotalPage(pagingResult.getTotalPages());
            msgListVO.setCurrentPage(pagingResult.getPageNo());
            for (MsgPO msgPO : pagingResult.getResult()) {
                MsgVO msgVO = new MsgVO();
                msgVO.setId(msgPO.getMsgId());
                msgVO.setOpenId(msgPO.getOpenId());
                msgVO.setContent(msgPO.getContent());
                msgVO.setHeadImgUrl(msgPO.getHeadImgUrl());
                msgVO.setIsFocus(msgPO.getIsFocus());
                msgVO.setName(msgPO.getNickName());
                msgVO.setTime(msgPO.getCreateTime());
                msgListVO.getMsgs().add(msgVO);
            }
            return msgListVO;
        }
        return null;
    }

}
