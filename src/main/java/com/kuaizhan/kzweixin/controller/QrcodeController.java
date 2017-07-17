package com.kuaizhan.kzweixin.controller;

import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.service.QrcodeService;
import org.springframework.web.bind.annotation.RequestMapping;
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


    @RequestMapping(value = "/qr/new")
    public JsonResponse newQrcode(@RequestParam(value = "site_id") long siteId,
                                  @RequestParam(value = "response_type") int respType,
                                  @RequestParam(value = "response_json") String respJson,
                                  @RequestParam(value = "qrcode_name") String qrName) {

        return new JsonResponse(null);
    }

}
