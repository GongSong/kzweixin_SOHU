package com.kuaizhan.kzweixin.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 拦截controller 实现参数校验
 * Created by liangjiateng on 2017/3/21.
 */
@Component
@Aspect
public class ControllerAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    @Pointcut("execution(* com.kuaizhan.kzweixin.controller.*.*(..))")
    private void controllerMethod() {

    }

    @Before("controllerMethod()")
    public void before(){
    }

    @Around("controllerMethod()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
    }

    /**
     * 日志输出
     */
    @AfterThrowing(value = "controllerMethod()", throwing = "e")
    public void afterThrowing(Throwable e) {

        if (e instanceof com.kuaizhan.kzweixin.exception.BusinessException) {
            // 业务异常只打info日志
            logger.info(e.toString());
        } else {
            logger.error("[Unknown Exception]", e);
        }

    }
}
