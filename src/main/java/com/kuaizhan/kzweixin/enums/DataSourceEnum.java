package com.kuaizhan.kzweixin.enums;

/**
 * Created by zixiong on 2017/08/06.
 */
public enum DataSourceEnum {
    // 默认数据源
    DEFAULT_DATASOURCE("defaultDataSource"),
    // 分片数据源
    SHARDING_DATASOURCE("shardingDataSource");

    private String value;
    DataSourceEnum(String value) {
        this.value = value;
    }
}
