package com.kuaizhan.controller;


import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.exception.business.AccountNotExistException;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DO.MsgDO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.pojo.VO.JsonResponse;
import com.kuaizhan.pojo.VO.MsgListVO;
import com.kuaizhan.pojo.VO.MsgVO;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.MsgService;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.*;

/**
 * 消息管理controller
 * Created by Mr.Jadyn on 2016/12/29.
 */
@RestController
@RequestMapping(value = ApplicationConfig.VERSION, produces = "application/json")
public class MsgController extends BaseController {

    @Resource
    MsgService msgService;
    @Resource
    AccountService accountService;

    /**
     * 获取消息列表
     *
     * @param siteId 站点id
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/msgs", method = RequestMethod.GET)
    public JsonResponse listMsgs(@RequestParam long siteId, @RequestParam int page, @RequestParam int isHide, @RequestParam(required = false) String keyword) throws RedisException, DaoException, AccountNotExistException {
        AccountDO accountDO = accountService.getAccountBySiteId(siteId);
        Page<MsgDO> pagingResult = msgService.listMsgsByPagination(siteId, accountDO.getAppId(), page, keyword, isHide);
        return new JsonResponse(handleData(pagingResult));
    }

    /**
     * 获取新发消息数量
     *
     * @param siteId 站点id
     * @return
     */
    @RequestMapping(value = "/msgs/new/count", method = RequestMethod.GET)
    public JsonResponse getNewMsgsNum(@RequestParam long siteId) throws DaoException, AccountNotExistException, RedisException {
        AccountDO accountDO = accountService.getAccountBySiteId(siteId);
        long num = msgService.countMsg(accountDO.getAppId(), 1, 0, null, 0);
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
    public JsonResponse listNewMsgs(@RequestParam long siteId) throws IOException, DaoException, AccountNotExistException, RedisException {
        AccountDO accountDO = accountService.getAccountBySiteId(siteId);
        List<MsgDO> msgs = msgService.listNewMsgs(accountDO.getAppId());
        if (msgs.size() > 0) {
            msgService.updateMsgsStatus(siteId, accountDO.getAppId(), msgs);
        }
        Page<MsgDO> pagingResult = msgService.listMsgsByPagination(siteId, accountDO.getAppId(), 1, null, 0);
        return new JsonResponse(handleData(pagingResult));
    }


    private MsgListVO handleData(Page<MsgDO> pagingResult) {
        if (pagingResult.getResult() != null) {
            MsgListVO msgListVO = new MsgListVO();
            msgListVO.setTotalNum(pagingResult.getTotalCount());
            msgListVO.setTotalPage(pagingResult.getTotalPages());
            msgListVO.setCurrentPage(pagingResult.getPageNo());
            for (MsgDO msgDO : pagingResult.getResult()) {
                MsgVO msgVO = new MsgVO();
                msgVO.setId(msgDO.getMsgId());
                msgVO.setOpenId(msgDO.getOpenId());
                msgVO.setContent(msgDO.getContent());
                msgVO.setHeadImgUrl(msgDO.getHeadImgUrl());
                msgVO.setIsFocus(msgDO.getIsFoucs());
                msgVO.setName(msgDO.getNickName());
                msgVO.setTime(msgDO.getCreateTime());
                msgListVO.getMsgs().add(msgVO);
            }
            return msgListVO;
        }
        return null;
    }

}
