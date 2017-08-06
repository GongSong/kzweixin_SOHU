package com.kuaizhan.kzweixin.aspect;

import com.kuaizhan.kzweixin.exception.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * controller aspect
 * Created by liangjiateng on 2017/3/21.
 */
@Aspect
public class ControllerAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    @Pointcut("execution(* com.kuaizhan.kzweixin.controller.*.*(..))")
    private void controllerMethod() {

    }

    @Before("controllerMethod()")
    public void before() {
    }

    @Around("controllerMethod()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
    }

    /**
     * 日志输出
     */
    @AfterThrowing(value = "controllerMethod()", throwing = "e")
    public void afterThrowing(Exception e) {
        if (e instanceof BusinessException) {
            // 业务异常只打info日志
            logger.info(e.toString());
        } else {
            logger.error("[Unknown Exception]", e);
        }
    }
}
