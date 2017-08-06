package com.kuaizhan.kzweixin.dao.base;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataSource自动路由
 * Created by zixiong on 2017/08/05.
 */
public class RoutingDataSource extends AbstractRoutingDataSource implements InitializingBean {

    private DataSource defaultDataSource;

    private DataSource shardingDataSource;

    private List<String> shardingMappers;

    public static final String SHARDING_DATASOURCE_KEY = "shardingDataSource";

    public static final String DEFAULT_DATASOURCE_KEY = "defaultDataSource";

    private ThreadLocal<String> dataSourceKeyThreadLocal = new InheritableThreadLocal<>();

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceKey = dataSourceKeyThreadLocal.get();
        // 强制指定dataSourceKey
        if (! SHARDING_DATASOURCE_KEY.equals(dataSourceKey) && ! DEFAULT_DATASOURCE_KEY.equals(dataSourceKey)) {
            throw new IllegalStateException("[RoutingDataSource] dataSourceKey must be 'shardingDataSource' or 'defaultDataSource'");
        }
        return dataSourceKey;
    }

    @Override
    public void afterPropertiesSet() {
        // 默认数据源
        this.setDefaultTargetDataSource(defaultDataSource);
        // key与数据源的映射关系
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(SHARDING_DATASOURCE_KEY, shardingDataSource);
        targetDataSources.put(DEFAULT_DATASOURCE_KEY, defaultDataSource);
        this.setTargetDataSources(targetDataSources);

        super.afterPropertiesSet();
    }

    /**
     * 暴露接口设置DataSourceKey
     * @param key DataSourceKey
     */
    public void setDataSourceKey(String key) {
        dataSourceKeyThreadLocal.set(key);
    }

    /* ----- getter and setter --- */
    public void setDefaultDataSource(DataSource defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
    }

    public void setShardingDataSource(DataSource shardingDataSource) {
        this.shardingDataSource = shardingDataSource;
    }

    public List<String> getShardingMappers() {
        return shardingMappers;
    }

    public void setShardingMappers(List<String> shardingMappers) {
        this.shardingMappers = shardingMappers;
    }

}
