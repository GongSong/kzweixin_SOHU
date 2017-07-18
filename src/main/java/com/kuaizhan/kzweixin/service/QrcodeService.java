package com.kuaizhan.kzweixin.service;

import com.kuaizhan.kzweixin.dao.po.auto.QrcodePO;

import java.util.List;

/**
 * 参数二维码service
 * Created by zixiong on 2017/6/1.
 */
public interface QrcodeService {

    /**
     * 获取临时的参数二维码
     * @param sceneId 场景id
     * @return 返回二维码图片地址
     */
    String getTmpQrcode(long weixinAppid, int sceneId);

    /**
     * 获取参数二维码
     * @return 返回二维码图片地址
     */
    String genQrcodeByWxAppId(long weixinAppid,int respType,String respJson,String qrName);

    List<QrcodePO> getQrcodeByWxAppId(long weixinAppid, String query);


}
