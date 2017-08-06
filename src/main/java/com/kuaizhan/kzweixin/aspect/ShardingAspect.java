package com.kuaizhan.kzweixin.aspect;

import com.kuaizhan.kzweixin.dao.base.RoutingDataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by zixiong on 2017/08/05.
 */
@Aspect
public class ShardingAspect {

    @Resource
    private RoutingDataSource routingDataSource;

    private Map<Object, String> keyMap = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(ShardingAspect.class);

    /**
     * 初始化，把配置为使用sharding的mapper放入keyMap
     */
    @PostConstruct
    public void init() throws Exception {
        for (String mapperClassName: routingDataSource.getShardingMappers()) {
            Class mapperClass = Class.forName(mapperClassName);
            keyMap.put(mapperClass, RoutingDataSource.SHARDING_DATASOURCE_KEY);
            logger.info("[ShardingAspect] config class {} to use shardingDataSource", mapperClass);
        }
    }

    @Pointcut("execution(* com.kuaizhan.kzweixin.dao.mapper.*..*(..))")
    private void mapperMethod() {
    }

    /**
     * mapper执行前，把mapper放入dict
     */
    @Around("mapperMethod()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        String dataSourceKey = RoutingDataSource.DEFAULT_DATASOURCE_KEY;
        Class mapperInterfaces[] = pjp.getTarget().getClass().getInterfaces();
        // 如果mapper的Interface在keyMap中，使用shardingDataSource，否则使用defaultDataSource
        for (Class mapperInterface: mapperInterfaces) {
            if (keyMap.containsKey(mapperInterface)) {
                dataSourceKey = RoutingDataSource.SHARDING_DATASOURCE_KEY;
                break;
            }
        }
        routingDataSource.setDataSourceKey(dataSourceKey);
        return pjp.proceed();
    }
}
