package com.kuaizhan.kzweixin.mq.consumer;

import com.kuaizhan.kzweixin.dao.mapper.auto.FanMapper;
import com.kuaizhan.kzweixin.dao.po.auto.FanPO;
import com.kuaizhan.kzweixin.dao.po.auto.FanPOExample;
import com.kuaizhan.kzweixin.mq.dto.FanDTO;
import com.kuaizhan.kzweixin.service.FanService;
import com.kuaizhan.kzweixin.utils.DBTableUtil;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by fangtianyu on 7/12/17.
 */
public class FanUpdateConsumer extends BaseConsumer{

    @Resource
    private FanService fanService;
    @Resource
    private FanMapper fanMapper;

    @Override
    public void onMessage(String message) {
        FanDTO fanDTO = JsonUtil.string2Bean(message, FanDTO.class);
        String appId = fanDTO.getAppId();
        String openId = fanDTO.getOpenId();
        fanService.addFanOpenId(appId, openId);

        FanPOExample example = new FanPOExample();
        example.createCriteria()
                .andAppIdEqualTo(appId)
                .andOpenIdEqualTo(openId)
                .andStatusEqualTo(1);
        String table = DBTableUtil.getFanTableName(appId);
        List<FanPO> fanPOList = fanMapper.selectByExample(example, table);

        if (fanPOList.isEmpty() || DateUtil.curSeconds() - fanPOList.get(0).getUpdateTime() > 3600) {
            fanService.addFan(appId, openId);
        }
    }
}
