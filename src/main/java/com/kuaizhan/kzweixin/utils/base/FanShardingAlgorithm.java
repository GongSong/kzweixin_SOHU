package com.kuaizhan.kzweixin.utils.base;

import com.kuaizhan.kzweixin.config.ApplicationConfig;

/**
 * Created by zixiong on 2017/08/03.
 */
public class FanShardingAlgorithm extends AppidTableAlgorithm {
    public FanShardingAlgorithm() {
        super(ApplicationConfig.FAN_TABLE_NUM);
    }
}
