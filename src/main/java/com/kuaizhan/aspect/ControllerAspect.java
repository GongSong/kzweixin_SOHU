package com.kuaizhan.aspect;

import com.kuaizhan.utils.LogUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;


/**
 * 拦截controller 实现参数校验
 * Created by liangjiateng on 2017/3/21.
 */
@Component
@Aspect
public class ControllerAspect {

    @Pointcut("execution(* com.kuaizhan.controller.*.*(..))")
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
        LogUtil.logMsg(e);
    }
}
