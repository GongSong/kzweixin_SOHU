package com.kuaizhan.kzweixin.service.impl;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.constant.KzExchange;
import com.kuaizhan.kzweixin.service.CommonService;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import com.kuaizhan.kzweixin.utils.MqUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zixiong on 2017/6/26.
 */
@Service
public class CommonServiceImpl implements CommonService {
    @Resource
    private MqUtil mqUtil;

    @Override
    public void kzStat(String traceId, String traceKey) {
        String msg = JsonUtil.bean2String(ImmutableMap.of(
                "trace_id", traceId,
                "trace_key", traceKey
        ));
        mqUtil.publish(KzExchange.KZ_STAT_INC, "", msg);
    }
}
