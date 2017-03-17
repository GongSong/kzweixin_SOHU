package com.kuaizhan.controller;


import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.exception.business.AccountNotExistException;
import com.kuaizhan.exception.business.ParamException;
import com.kuaizhan.exception.business.TagException;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DO.FanDO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.pojo.DTO.TagDTO;
import com.kuaizhan.pojo.VO.FanListVO;
import com.kuaizhan.pojo.VO.FanVO;
import com.kuaizhan.pojo.VO.JsonResponse;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.FanService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * 粉丝管理Controller
 * Created by Mr.Jadyn on 2016/12/29.
 */
@RestController
@RequestMapping(value = ApplicationConfig.VERSION, produces = "application/json")
public class FanController extends BaseController {

    @Resource
    FanService fansService;
    @Resource
    AccountService accountService;

    /**
     * 获取粉丝列表
     *
     * @return
     */
    @RequestMapping(value = "/fans", method = RequestMethod.GET)
    public JsonResponse listFanByPagination(@RequestParam long siteId, @RequestParam int page, @RequestParam(required = false) List<Integer> tagIds, @RequestParam int isBlack, @RequestParam(required = false) String keyword) throws RedisException, DaoException, AccountNotExistException, ParamException {
        AccountDO accountDO = accountService.getAccountBySiteId(siteId);
        if (accountDO == null) {
            throw new AccountNotExistException();
        }
        if (page < 1) {
            throw new ParamException();
        }
        Page<FanDO> fanDOPage = fansService.listFanByPagination(siteId, accountDO.getAppId(), page, isBlack, tagIds, keyword);
        List<FanDO> fanDOList = fanDOPage.getResult();
        FanListVO fanListVO = new FanListVO();
        if (fanDOList != null) {
            fanListVO.setTotalNum(fanDOPage.getTotalCount());
            fanListVO.setCurrentPage(fanDOPage.getPageNo());
            fanListVO.setTotalPage(fanDOPage.getTotalPages());
            for (FanDO fanDO : fanDOList) {
                FanVO fanVO = new FanVO();
                fanVO.setId(fanDO.getFanId());
                fanVO.setName(fanDO.getNickName());
                fanVO.setAddress(fanDO.getCountry() + " " + fanDO.getProvince() + " " + fanDO.getCity());
                fanVO.setSex(fanDO.getSex());
                fanVO.setAvatar(fanDO.getHeadImgUrl());
                fanVO.setFocusTime(fanDO.getSubscribeTime());
                fanVO.setOpenId(fanDO.getOpenId());
                fanVO.setTags(fanDO.getTagIdsJson());
                fanListVO.getFans().add(fanVO);
            }
        }
        return new JsonResponse(fanListVO);
    }

    /**
     * 获取所有标签
     *
     * @param siteId 站点Id
     * @return
     */
    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public JsonResponse listTags(@RequestParam long siteId) throws RedisException, DaoException, AccountNotExistException, TagException {
        AccountDO accountDO = accountService.getAccountBySiteId(siteId);
        if (accountDO == null) {
            throw new AccountNotExistException();
        }
        List<TagDTO> list = fansService.listTags(siteId,accountDO.getAccessToken());
        return new JsonResponse(list);
    }

}


