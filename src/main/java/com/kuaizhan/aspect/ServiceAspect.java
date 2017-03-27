package com.kuaizhan.aspect;

import com.kuaizhan.exception.BaseException;
import com.kuaizhan.exception.business.BusinessException;
import com.kuaizhan.exception.system.SystemException;
import com.kuaizhan.utils.LogUtil;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


/**
 * 拦截service 记录日志
 * Created by liangjiateng on 2017/3/6.
 */
@Component
@Aspect
public class ServiceAspect {


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
        LogUtil.logMsg((Exception) ex);
    }


}
