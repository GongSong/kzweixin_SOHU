package com.kuaizhan.kzweixin.mq.consumer;

import com.kuaizhan.kzweixin.mq.dto.AfterBindDTO;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.utils.JsonUtil;

import javax.annotation.Resource;

/**
 * 用户新绑定后做的事情
 * Created by zixiong on 2017/6/19.
 */
public class AfterBindConsumer extends  BaseConsumer{

    @Resource
    private AccountService accountService;

    @Override
    public void onMessage(String message) {
        AfterBindDTO dto = JsonUtil.string2Bean(message, AfterBindDTO.class);
        accountService.uploadQrcode2Kz(dto.getWeixinAppid());
    }
}
