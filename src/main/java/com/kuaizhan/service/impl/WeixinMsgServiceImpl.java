package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.exception.business.AccountNotExistException;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.EncryptException;
import com.kuaizhan.exception.system.XMLParseException;
import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.WeixinFanService;
import com.kuaizhan.service.WeixinMsgService;
import com.kuaizhan.utils.weixin.WXBizMsgCrypt;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by liangjiateng on 2017/3/20.
 */
@Service("weixinMsgService")
public class WeixinMsgServiceImpl implements WeixinMsgService {

    @Resource
    WeixinFanService weixinFanService;
    @Resource
    AccountService accountService;

    @Override
    public String handlePushMsg(String appId, String signature, String timestamp, String nonce, String postData) throws EncryptException, XMLParseException, DaoException, AccountNotExistException {
        //TODO:使用MQ进行处理
        AccountDO accountDO = accountService.getAccountByAppId(appId);
        if (accountDO == null) {
            throw new AccountNotExistException();
        }
        //解密消息
        String msg;
        try {
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(ApplicationConfig.WEIXIN_TOKEN, ApplicationConfig.WEIXIN_AES_KEY, ApplicationConfig.WEIXIN_APPID_THIRD);
            msg = wxBizMsgCrypt.decryptMsg(signature, timestamp, nonce, postData);
        } catch (Exception e) {
            throw new EncryptException(e.getMessage());
        }
        //解析消息
        Document document;
        try {
            document = DocumentHelper.parseText(msg);
        } catch (Exception e) {
            throw new XMLParseException(e.getMessage());
        }
        Element root = document.getRootElement();
        Element msgType = root.element("MsgType");
        Element event = root.element("Event");
        if (event != null) {
            switch (event.getText()) {
                //关注
                case "subscribe":
                    weixinFanService.subscribe(accountDO, msg);
                    break;
                //取关
                case "unsubscribe":
                    weixinFanService.unSubscribe(accountDO, msg);
                    break;
                //群发
                case "MASSSENDJOBFINISH":
                    break;
            }

        } else if (msgType != null) {

        }
        return "success";
    }
}
