package com.kuaizhan.kzweixin.service;

import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.dao.po.auto.CustomMassPO;
import com.kuaizhan.kzweixin.dao.po.auto.MassPO;
import com.kuaizhan.kzweixin.enums.MsgType;

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
     * @param wxAppId
     * @return
     */
    CustomMassPO getCustomMassById(long wxAppId);

    /**
     * 获取客服群发
     * @param wxAppid
     * @return
     */
    List<CustomMassPO> getCustomMassByWxAppId(long wxAppid);


    String formatMassResponseJson(String json, MassPO.RespType type);

    /**
     * 返回群发消息的MassMsg对象
     * @param type 参考MsgTyoe
     * @param json 图文{[1,2,3]} 文字{"content":"sss.."} 图片{"media_id":"xxx","pic_url":"xxx"}
     * @param isMulti 是否多图文
     * @return
     */
    Object wrapMassMsg(long weixinAppid, MassPO.RespType type, String json, int isMulti);

    /**
     * 返回群发消息的CustomMsg Json字符串
     * @param weixinAppid
     * @param type
     * @param json  图文{[1,2,3]} 文字{"content":"sss.."} 图片{"media_id":"xxx","pic_url":"xxx"}
     * @param isMulti
     * @return
     */
    String wrapPreviewMsg(long weixinAppid, MassPO.RespType type, String json, int isMulti);

    MsgType convert2WxMsgType(MassPO.RespType respType);

    /**
     * 发送群发消息
     * @param weixinAppid
     * @param msgType
     * @param contentObj
     */
    String sendMassMsg(long weixinAppid, int tagId, MassPO.RespType msgType, Object contentObj);

    /**
     * 删除定时任务
     * @param pubTime
     */
    JsonResponse deleteTimingJob(long pubTime, long massId);

    /**
     * 创建定时任务
     * @param pubTime
     */
    JsonResponse CreateTimingJob(long pubTime, long massId);

    /**
     * 生成MassID
      * @return
     */
    long genMassId();

    /**
     * 更新
     * @param mass
     */
    void updateMass(MassPO mass);

    /**
     * 插入
     * @param mass
     */
    void insertMass(MassPO mass);

    /**
     * 检查是否支持此type
     * @param type
     * @return
     */
    boolean checkSupportType(MassPO.RespType type);
}
