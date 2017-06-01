package com.kuaizhan.service.impl;

import com.kuaizhan.config.WxApiConfig;
import com.kuaizhan.manager.WxCommonManager;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.QrcodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zixiong on 2017/6/1.
 */
@Service("qrcodeService")
public class QrcodeServiceImpl implements QrcodeService {

    @Resource
    private AccountService accountService;

    @Override
    public String getTmpQrcode(long weixinAppid, int sceneId) {
        String accessToken = accountService.getAccessToken(weixinAppid);

        String ticket = WxCommonManager.genTmpQrcode(accessToken, sceneId);
        return WxApiConfig.qrcodeUrl(ticket);
    }
}
