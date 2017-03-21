package com.kuaizhan.aspect;

import com.kuaizhan.exception.BaseException;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * 拦截service 记录日志
 * Created by liangjiateng on 2017/3/6.
 */
@Component
@Aspect
public class ServiceAspect {

    private static Logger logger = Logger.getLogger(ServiceAspect.class);

    @Pointcut("execution(* com.kuaizhan.service.impl.*.*(..))")
    private void serviceMethod() {

    }



    /**
     * 日志输出
     *
     * @param ex
     */
    @AfterThrowing(value = "serviceMethod()", throwing = "ex")
    public void afterThrowing(Throwable ex) {

        if (ex instanceof BaseException) {
            logger.error(output(((BaseException) ex).getDate(), ((BaseException) ex).getMsg(), ((BaseException) ex).getErrorStack()));
        }
    }

    private String output(long time, String msg, String errorStack) {
        return "Time:" + new Date(time).toString() + ";\nMessage:" + msg + "\nOriginal info:" + errorStack + ";";
    }
}
