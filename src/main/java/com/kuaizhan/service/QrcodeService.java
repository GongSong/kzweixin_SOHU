package com.kuaizhan.service;

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
}
