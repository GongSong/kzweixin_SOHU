package com.kuaizhan.kzweixin.controller;

import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.dao.po.auto.AccountPO;
import com.kuaizhan.kzweixin.dao.po.auto.MassPO;
import com.kuaizhan.kzweixin.dao.po.auto.QrcodePO;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.QrcodeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by steffanchen on 2017/7/17.
 */


@RestController
@RequestMapping(value = AppConstant.VERSION, produces = "application/json")
public class QrcodeController  extends BaseController {

    @Resource
    protected QrcodeService qrcodeService;

    @Resource
    protected AccountService accountService;

    @RequestMapping(value = "/qr/new")
    public JsonResponse newQrcode(@RequestParam(value = "site_id") long siteId,
                                  @RequestParam(value = "response_type") int respType,
                                  @RequestParam(value = "response_json") String respJson,
                                  @RequestParam(value = "qrcode_name") String qrName) {

        return new JsonResponse(null);
    }



    /**
     * 二维码列表 @url: ajax-qrcode-list-get
     * @param siteId
     * @param pageNO
     * @param pageSize
     * @param query
     * @return
     */
    @RequestMapping(value = "/qr/list", method = RequestMethod.POST)
    public JsonResponse getQrcodeList(@RequestParam(value = "siteId") long siteId,
                                    @RequestParam(value = "pageNO", defaultValue = "1") int pageNO,
                                    @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                    @RequestParam(value = "query", required = false, defaultValue = "") String query) {
        List<QrcodePO> qrcodeList= null;
        AccountPO accountPO = accountService.getAccountBySiteId(siteId);
        if(accountPO != null && accountPO.getWeixinAppid() != 0) {
            qrcodeList = qrcodeService.getQrcodeByWxAppId(accountPO.getWeixinAppid(),query);
        }
        return new JsonResponse(qrcodeList);
    }



}
