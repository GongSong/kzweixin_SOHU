package com.kuaizhan.service;

import com.kuaizhan.exception.business.AccountNotExistException;
import com.kuaizhan.exception.business.SendCustomMsgException;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.EncryptException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.exception.system.XMLParseException;
import org.json.JSONObject;

/**
 * 微信消息服务
 * Created by liangjiateng on 2017/3/20.
 */
public interface WeixinMsgService {

    /**
     * 处理微信推送来的消息
     *
     * @return
     */
    String handleWeixinPushMsg(String appId, String signature, String timestamp, String nonce, String postData) throws EncryptException, XMLParseException, DaoException, AccountNotExistException, RedisException;

    /**
     * 给用户发送客服消息
     */

    int sendCustomMsg(String appId, String accessToken, String openId, int msgType, JSONObject content) throws SendCustomMsgException;

}
