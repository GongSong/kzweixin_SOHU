package com.kuaizhan.kzweixin.service;

import com.kuaizhan.kzweixin.dao.po.auto.CustomMassPO;
import com.kuaizhan.kzweixin.dao.po.auto.MassPO;

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
     *
     * @param wxAppId
     * @return
     */
    List<MassPO> getMassByWxAppId(long wxAppId);


    List<CustomMassPO> getCustomMassByWxAppId(long wxAppid);

}
