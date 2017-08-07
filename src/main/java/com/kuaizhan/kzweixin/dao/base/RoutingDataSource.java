package com.kuaizhan.kzweixin.dao.base;

import com.kuaizhan.kzweixin.enums.DataSourceEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * DataSource自动路由
 * Created by zixiong on 2017/08/05.
 */
public class RoutingDataSource extends AbstractRoutingDataSource implements InitializingBean {

    private DataSource defaultDataSource;

    private DataSource shardingDataSource;

    private ThreadLocal<DataSourceEnum> dataSourceKeyThreadLocal = new InheritableThreadLocal<>();

    private static Logger logger = LoggerFactory.getLogger(RoutingDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        DataSourceEnum dataSourceKey = dataSourceKeyThreadLocal.get();
        logger.info("[RoutingDataSource] determine to use {}", dataSourceKey);
        return dataSourceKey;
    }

    @Override
    public void afterPropertiesSet() {
        // 默认数据源
        this.setDefaultTargetDataSource(defaultDataSource);
        // key与数据源的映射关系
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceEnum.DEFAULT_DATASOURCE, defaultDataSource);
        targetDataSources.put(DataSourceEnum.SHARDING_DATASOURCE, shardingDataSource);
        this.setTargetDataSources(targetDataSources);

        super.afterPropertiesSet();
    }

    /**
     * 暴露接口设置DataSourceKey
     * @param dataSourceKey DataSourceKey
     */
    public void setDataSourceKey(DataSourceEnum dataSourceKey) {
        dataSourceKeyThreadLocal.set(dataSourceKey);
        logger.info("[RoutingDataSource] DataSource be set to {}", dataSourceKey);
    }

    /* ----- getter and setter --- */
    public void setDefaultDataSource(DataSource defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
    }

    public void setShardingDataSource(DataSource shardingDataSource) {
        this.shardingDataSource = shardingDataSource;
    }
}
