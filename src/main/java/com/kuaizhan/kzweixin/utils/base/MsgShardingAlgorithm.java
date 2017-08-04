package com.kuaizhan.kzweixin.utils.base;

import com.kuaizhan.kzweixin.config.ApplicationConfig;

/**
 * Created by zixiong on 2017/08/03.
 */
public class MsgShardingAlgorithm extends AppidTableAlgorithm{
    public MsgShardingAlgorithm() {
        super(ApplicationConfig.MSG_TABLE_NUM);
    }
}
