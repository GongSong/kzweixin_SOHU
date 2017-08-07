package com.kuaizhan.kzweixin.aspect;

import com.kuaizhan.kzweixin.dao.base.RoutingDataSource;
import com.kuaizhan.kzweixin.enums.DataSourceEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by zixiong on 2017/08/05.
 */
@Aspect
public class ShardingAspect {

    @Resource
    private RoutingDataSource routingDataSource;

    private Set<String> shardingMappers;

    public void setShardingMappers(Set<String> shardingMappers) {
        this.shardingMappers = shardingMappers;
    }

    @Pointcut("execution(* com.kuaizhan.kzweixin.dao.mapper.*..*(..))")
    private void mapperMethod() {
    }

    /**
     * 根据mapper决定数据源
     */
    @Around("mapperMethod()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        DataSourceEnum dataSourceKey = DataSourceEnum.DEFAULT_DATASOURCE;
        Class mapperInterfaces[] = pjp.getTarget().getClass().getInterfaces();

        // 如果mapper的Interface在keyMap中，使用shardingDataSource，否则使用defaultDataSource
        for (Class mapperInterface: mapperInterfaces) {
            if (shardingMappers.contains(mapperInterface.getName())) {
                dataSourceKey = DataSourceEnum.SHARDING_DATASOURCE;
                break;
            }
        }
        routingDataSource.setDataSourceKey(dataSourceKey);
        return pjp.proceed();
    }
}
