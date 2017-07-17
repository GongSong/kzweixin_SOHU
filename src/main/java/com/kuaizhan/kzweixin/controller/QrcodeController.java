package com.kuaizhan.kzweixin.controller;

import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.dao.po.auto.AccountPO;
import com.kuaizhan.kzweixin.dao.po.auto.MassPO;
import com.kuaizhan.kzweixin.service.QrcodeService;
import com.kuaizhan.kzweixin.service.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;

/**
 * Created by steffanchen on 2017/7/17.
 */


@RestController
@RequestMapping(value = AppConstant.VERSION, produces = "application/json")
public class QrcodeController {

    @Resource
    protected QrcodeService qrcodeService;

    @Resource
    protected AccountService accountService;

    /**
     * 二维码列表 @url: ajax-qrcode-list-get
     * @param siteId
     * @param pageNO
     * @param pageSize
     * @param query
     * @return
     */
    @RequestMapping(value = "/qrcode/list", method = RequestMethod.POST)
    public JsonResponse getQrcodeList(@RequestParam(value = "siteId") long siteId,
                                    @RequestParam(value = "pageNO", defaultValue = "1") int pageNO,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                    @RequestParam(value = "query", required = false, defaultValue = "") String query) {
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        if(accountPO != null && accountPO.getWeixinAppid() != 0) {
            qrcodeList = qrcodeService.getQrcodeByWxAppId(accountPO.getWeixinAppid());
        }
        return new JsonResponse(qrcodeList);
    }


    
}
