package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.config.WxApiConfig;
import com.kuaizhan.kzweixin.manager.WxCommonManager;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.QrcodeService;
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
