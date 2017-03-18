package com.kuaizhan.controller;


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
@RequestMapping(value = "/v1", produces = "application/json")
public class MsgController extends BaseController{

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
            return new JsonResponse(msgListVO);
        }
        return new JsonResponse(null);
    }


}
