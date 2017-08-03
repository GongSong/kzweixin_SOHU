package com.kuaizhan.kzweixin.utils.base;

import com.kuaizhan.kzweixin.config.ApplicationConfig;

/**
 * Created by zixiong on 2017/08/03.
 */
public class OpenIdShardingAlgorithm extends AppidTableAlgorithm {
    public OpenIdShardingAlgorithm() {
        super(ApplicationConfig.OPEN_ID_TABLE_NUM);
    }
}
