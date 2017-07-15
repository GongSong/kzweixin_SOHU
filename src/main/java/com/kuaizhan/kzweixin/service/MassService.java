package com.kuaizhan.kzweixin.service;

import com.kuaizhan.kzweixin.dao.po.auto.CustomMassPO;
import com.kuaizhan.kzweixin.dao.po.auto.MassPO;
import com.kuaizhan.kzweixin.enums.MsgType;
import com.kuaizhan.kzweixin.exception.BusinessException;

import java.util.List;

/**
 * 消息群发
 * Created by Chen on 17/7/10.
 */
public interface MassService {

    /**
     * 获取群发消息
     * @param id
     * @return
     */
    MassPO getMassById(long id);

    /**
     * 获取群发消息
     * @param wxAppId
     * @return
     */
    List<MassPO> getMassByWxAppId(long wxAppId);

    /**
     * 获取客服群发
     * @param wxAppid
     * @return
     */
    List<CustomMassPO> getCustomMassByWxAppId(long wxAppid);

    /**
     * 返回群发消息的MassMsg对象
     * @param type 参考MsgTyoe
     * @param json
     * @param isMulti 是否多图文
     * @return
     */
    Object wrapMassMsg(long weixinAppid, MsgType type, String json, int isMulti);

    /**
     * 返回群发消息的CustomMsg Json字符串
     * @param weixinAppid
     * @param type
     * @param json
     * @param isMulti
     * @return
     */
    String wrapPreviewMsg(long weixinAppid, MsgType type, String json, int isMulti);

    /**
     * 发送群发消息
     * @param weixinAppid
     * @param msgType
     * @param contentObj
     */
    void sendMassMsg(long weixinAppid, int tagId, MsgType msgType, Object contentObj);

}
